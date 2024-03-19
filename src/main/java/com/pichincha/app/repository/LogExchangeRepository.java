package com.pichincha.app.repository;

import com.pichincha.app.model.Exchange;
import com.pichincha.app.model.LogExchange;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface LogExchangeRepository extends ReactiveCrudRepository<LogExchange, Integer> {

}
