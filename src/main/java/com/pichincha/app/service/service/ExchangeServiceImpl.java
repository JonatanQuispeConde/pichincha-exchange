package com.pichincha.app.service.service;

import com.pichincha.app.dto.ExchangeDto;
import com.pichincha.app.dto.LogExchangeDto;
import com.pichincha.app.exception.CustomException;
import com.pichincha.app.model.Exchange;
import com.pichincha.app.model.LogExchange;
import com.pichincha.app.repository.ExchangeRepository;
import com.pichincha.app.security.model.User;
import com.pichincha.app.security.repository.UserRepository;
import com.pichincha.app.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {

    private final static String NF_MESSAGE = "exchange: not found";
    private final static String NAME_MESSAGE = "exchange: el tipo de moneda ya existe";
    private final static String NAME_MESSAGE_ALL = "exchange: uno o mas tipos de moneda ingresados ya existe";

    private final static String CHANGE_MESSAGE = "exchange: Una o más monedas no encontradas";

    @Autowired
    ExchangeRepository exchangeRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    LogExchangeServiceImpl logExchangeService;

    private final Authentication authentication = null;

    @Override
    public Flux<Exchange> getAll() {
        return exchangeRepository.findAll();
    }

    @Override
    public Mono<Exchange> getById(int id) {
        return exchangeRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, NF_MESSAGE)));
    }

    @Override
    public Mono<Exchange> save(ExchangeDto dto) {
        Mono<Boolean> existsName = exchangeRepository.findByTypeCurrency(dto.getTypeCurrency()).hasElement();
        return existsName.flatMap(exists -> exists ? Mono.error(new CustomException(HttpStatus.BAD_REQUEST, NAME_MESSAGE))
                : exchangeRepository.save(
                Exchange.builder()
                        .typeCurrency(dto.getTypeCurrency())
                        .unit(dto.getUnit())
                        .valueSoles(dto.getValueSoles())
                        .build()));
    }

    @Override
    public Flux<Exchange> saveAll(List<ExchangeDto> dtoList) {
        return Flux.fromIterable(dtoList)
                .flatMap(dto -> exchangeRepository.findByTypeCurrency(dto.getTypeCurrency()).hasElement()
                        .flatMap(exists -> exists ? Mono.error(new CustomException(HttpStatus.BAD_REQUEST, NAME_MESSAGE_ALL))
                                : exchangeRepository.save(
                                Exchange.builder()
                                        .typeCurrency(dto.getTypeCurrency())
                                        .unit(dto.getUnit())
                                        .valueSoles(dto.getValueSoles())
                                        .build())))
                .collectList()
                .flatMapMany(exchangeRepository::saveAll);
    }

    @Override
    public Mono<Exchange> update(int id, ExchangeDto dto) {
        Mono<Boolean> exchangeId = exchangeRepository.findById(id).hasElement();
        Mono<Boolean> exchangeRepeatedName = exchangeRepository.repeatedName(id, dto.getTypeCurrency()).hasElement();
        return exchangeId.flatMap(
                existsId -> existsId ?
                        exchangeRepeatedName.flatMap(existsName -> existsName ? Mono.error(new CustomException(HttpStatus.BAD_REQUEST, NAME_MESSAGE))
                                : exchangeRepository.save(new
                                Exchange(id, dto.getTypeCurrency(), dto.getUnit(), dto.getValueSoles())))
                        : Mono.error(new CustomException(HttpStatus.NOT_FOUND, NF_MESSAGE)));
    }

    @Override
    public Mono<Void> delete(int id) {
        Mono<Boolean> exchangeId = exchangeRepository.findById(id).hasElement();
        return exchangeId.flatMap(exists -> exists ? exchangeRepository.deleteById(id) : Mono.error(new CustomException(HttpStatus.NOT_FOUND, NF_MESSAGE)));
    }

    @Override
    public Mono<LogExchangeDto> changeMoney(LogExchangeDto exchangeDto) {

        Mono<User> user = getUserCurrent();
        Mono<Boolean> exchangeMoneyExist = exchangeRepository.findById(exchangeDto.getIdExchangeMoney()).hasElement();
        Mono<Boolean> exchangeQuoteExist = exchangeRepository.findById(exchangeDto.getIdExchangeQuote()).hasElement();

        Mono<Exchange> exchangeMoneyMono = exchangeRepository.findById(exchangeDto.getIdExchangeMoney()).filter(Objects::nonNull);
        Mono<Exchange> exchangeQuoteMono = exchangeRepository.findById(exchangeDto.getIdExchangeQuote()).filter(Objects::nonNull);

        return Mono.zip(exchangeMoneyExist, exchangeQuoteExist, exchangeMoneyMono, exchangeQuoteMono, user)
                .flatMap(tuple -> {
                    boolean moneyExist = tuple.getT1();
                    boolean quoteExist = tuple.getT2();

                    if (!moneyExist || !quoteExist) {
                        return Mono.error(new CustomException(HttpStatus.NOT_FOUND, CHANGE_MESSAGE));
                    }
                    Exchange exchangeMoney = tuple.getT3();
                    Exchange exchangeQuote = tuple.getT4();
                    User usertuple = tuple.getT5();

                    double exchangeRate = exchangeQuote.getValueSoles() / exchangeMoney.getValueSoles();
                    double result = exchangeDto.getAmount() * exchangeRate;

                    LogExchange logExchange = LogExchange.builder()
                            .idExchangeMoney(exchangeMoney.getId())
                            .valueExchangeMoney(exchangeMoney.getValueSoles())
                            .idExchangeQuote(exchangeQuote.getId())
                            .valueExchangeQuote(exchangeQuote.getValueSoles())
                            .unit(exchangeDto.getAmount())
                            .totalValue(result)
                            .userChanged(usertuple.getUsername())
                            .build();

                    return logExchangeService.save(logExchange)
                            .map(savedLogExchange -> LogExchangeDto.builder()
                                    .idExchangeMoney(savedLogExchange.getIdExchangeMoney())
                                    .valueExchangeMoney(savedLogExchange.getValueExchangeMoney())
                                    .exchangeMoneyName(exchangeMoney.getTypeCurrency())
                                    .idExchangeQuote(savedLogExchange.getIdExchangeQuote())
                                    .valueExchangeQuote(savedLogExchange.getValueExchangeQuote())
                                    .exchangeQuoteName(exchangeQuote.getTypeCurrency())
                                    .amount(savedLogExchange.getUnit())
                                    .amountChanged(savedLogExchange.getTotalValue())
                                    .build());
                });
    }

    private Mono<User> getUserCurrent() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    return userRepository.findByUsername(authentication.getName());
                });
    }
}
