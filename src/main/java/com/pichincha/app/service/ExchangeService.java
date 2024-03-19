package com.pichincha.app.service;

import com.pichincha.app.dto.ExchangeDto;
import com.pichincha.app.dto.LogExchangeDto;
import com.pichincha.app.model.Exchange;
import com.pichincha.app.model.LogExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ExchangeService {

    Flux<Exchange> getAll();
    Mono<Exchange> getById(int id);
    Mono<Exchange> save(ExchangeDto dto);
    Mono<Exchange> update(int id, ExchangeDto dto);
    Mono<Void> delete(int id);
    Flux<Exchange> saveAll(List<ExchangeDto> dtoList);
    Mono<LogExchangeDto> changeMoney(LogExchangeDto requestDto);
}
