package com.pichincha.app.security.controller;

import com.pichincha.app.security.dto.CreateUserDto;
import com.pichincha.app.security.dto.LoginDto;
import com.pichincha.app.security.dto.TokenDto;
import com.pichincha.app.security.model.User;
import com.pichincha.app.security.service.UserService;
import com.pichincha.app.security.service.UserServiceImpl;
import com.pichincha.app.validation.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j

public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    ObjectValidator objectValidator;

    public Mono<ServerResponse> login(ServerRequest request) {
        Mono<LoginDto> dtoMono = request.bodyToMono(LoginDto.class).doOnNext(objectValidator::validate);
        return dtoMono
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService.login(dto), TokenDto.class));
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<CreateUserDto> dtoMono = request.bodyToMono(CreateUserDto.class).doOnNext(objectValidator::validate);
        return dtoMono
                .flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService.create(dto), User.class));
    }
}
