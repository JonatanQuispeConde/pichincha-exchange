package com.pichincha.app.repository;

import com.pichincha.app.model.Exchange;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ExchangeRepository extends ReactiveCrudRepository<Exchange, Integer> {
    Mono<Exchange> findByTypeCurrency (String typeCurrency);

    @Query("SELECT * FROM exchange WHERE id <> :id AND type_currency = :typeCurrency")
    Mono<Exchange> repeatedName(int id, String typeCurrency);
}
