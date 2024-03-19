package com.pichincha.app.service.service;

import com.pichincha.app.dto.LogExchangeDto;
import com.pichincha.app.exception.CustomException;
import com.pichincha.app.model.Exchange;
import com.pichincha.app.model.LogExchange;
import com.pichincha.app.repository.ExchangeRepository;
import com.pichincha.app.repository.LogExchangeRepository;
import com.pichincha.app.security.model.User;
import com.pichincha.app.service.LogExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class LogExchangeServiceImpl implements LogExchangeService {

    private final static String NF_MESSAGE = "log exchange: not found";

    @Autowired
    LogExchangeRepository logExchangeRepository;

    @Autowired
    ExchangeRepository exchangeRepository;
;
    @Override
    public Flux<LogExchangeDto> getAll() {
        return logExchangeRepository.findAll()
                .flatMap(logExchange -> {
                    Mono<Exchange> exchangeMoneyMono = exchangeRepository.findById(logExchange.getIdExchangeMoney());
                    Mono<Exchange> exchangeQuoteMono = exchangeRepository.findById(logExchange.getIdExchangeQuote());


                    return Mono.zip(exchangeMoneyMono, exchangeQuoteMono)
                            .map(tuple -> {
                                Exchange exchangeMoney = tuple.getT1();
                                Exchange exchangeQuote = tuple.getT2();

                                return LogExchangeDto.builder()
                                        .idExchangeMoney(logExchange.getIdExchangeMoney())
                                        .valueExchangeMoney(logExchange.getValueExchangeMoney())
                                        .exchangeMoneyName(exchangeMoney.getTypeCurrency())
                                        .idExchangeQuote(logExchange.getIdExchangeQuote())
                                        .valueExchangeQuote(logExchange.getValueExchangeQuote())
                                        .exchangeQuoteName(exchangeQuote.getTypeCurrency())
                                        .amount(logExchange.getUnit())
                                        .userChanged(logExchange.getUserChanged())
                                        .build();
                            });
                });
    }

    @Override
    public Mono<LogExchange> save(LogExchange logExchange) {

        if(logExchange == null){
            return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, NF_MESSAGE));
        }
        return logExchangeRepository.save(logExchange);
    }


}
