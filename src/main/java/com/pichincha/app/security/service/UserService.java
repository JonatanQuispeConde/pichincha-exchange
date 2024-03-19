package com.pichincha.app.security.service;

import com.pichincha.app.security.dto.CreateUserDto;
import com.pichincha.app.security.dto.LoginDto;
import com.pichincha.app.security.dto.TokenDto;
import com.pichincha.app.security.model.User;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<TokenDto> login(LoginDto dto);
    Mono<User> create(CreateUserDto dto);
}
