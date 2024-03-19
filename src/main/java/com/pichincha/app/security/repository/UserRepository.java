package com.pichincha.app.security.repository;

import com.pichincha.app.security.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Integer> {

    Mono<User> findByUsername(String username);
}
