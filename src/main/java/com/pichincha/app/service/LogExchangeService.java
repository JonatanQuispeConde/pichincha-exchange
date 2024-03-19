package com.pichincha.app.service;

import com.pichincha.app.dto.LogExchangeDto;
import com.pichincha.app.model.LogExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LogExchangeService {

    Flux<LogExchangeDto> getAll();
    Mono<LogExchange> save(LogExchange logExchange);
}
