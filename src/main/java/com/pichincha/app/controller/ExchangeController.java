package com.pichincha.app.controller;

import com.pichincha.app.dto.ExchangeDto;
import com.pichincha.app.dto.LogExchangeDto;
import com.pichincha.app.model.Exchange;
import com.pichincha.app.model.LogExchange;
import com.pichincha.app.service.ExchangeService;
import com.pichincha.app.service.service.ExchangeServiceImpl;
import com.pichincha.app.service.service.LogExchangeServiceImpl;
import com.pichincha.app.validation.ObjectValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ExchangeController {

    @Autowired
    ExchangeService exchangeService;

    @Autowired
    ObjectValidator objectValidator;

    @Autowired
    LogExchangeServiceImpl logExchangeService;

    public Mono<ServerResponse> getAll(ServerRequest request) {
        Flux<Exchange> exchanges = exchangeService.getAll();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(exchanges, Exchange.class);
    }

    public Mono<ServerResponse> getOneById(ServerRequest request) {
        int id = Integer.parseInt(request.pathVariable("id"));
        Mono<Exchange> exchange = exchangeService.getById(id);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(exchange, Exchange.class);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<ExchangeDto> dtoMono = request.bodyToMono(ExchangeDto.class).doOnNext(objectValidator::validate);
        return dtoMono.flatMap(exchangeDto ->
                ServerResponse.ok().
                        contentType(MediaType.APPLICATION_JSON).body(exchangeService.save(exchangeDto), Exchange.class));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Mono<ServerResponse> saveAll(ServerRequest request) {
        Flux<ExchangeDto> dtoFlux = request.bodyToFlux(ExchangeDto.class).doOnNext(objectValidator::validate);

        return dtoFlux.collectList().flatMap(dtoList ->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(exchangeService.saveAll(dtoList), Exchange.class)
        );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Mono<ServerResponse> update(ServerRequest request) {
        int id = Integer.parseInt(request.pathVariable("id"));
        Mono<ExchangeDto> dtoMono = request.bodyToMono(ExchangeDto.class).doOnNext(objectValidator::validate);
        return dtoMono.flatMap(exchangeDto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(exchangeService.update(id, exchangeDto), Exchange.class));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Mono<ServerResponse> delete(ServerRequest request) {
        int id = Integer.parseInt(request.pathVariable("id"));
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(exchangeService.delete(id), Exchange.class);
    }

    public Mono<ServerResponse> changeMoney(ServerRequest request) {
        Mono<LogExchangeDto> requestMono = request.bodyToMono(LogExchangeDto.class).doOnNext(objectValidator::validate);;
        return requestMono.flatMap(requestDto ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
                        exchangeService.changeMoney(requestDto), LogExchange.class));
    }

    public Mono<ServerResponse> getLogsAll(ServerRequest request) {
        Flux<LogExchangeDto> logExchanges = logExchangeService.getAll();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(logExchanges, LogExchangeDto.class);
    }

}
