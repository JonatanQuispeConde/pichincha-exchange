package com.pichincha.app.security.router;

import com.pichincha.app.security.controller.AuthController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Slf4j
public class RouterAuth {

    private static final String PATH = "auth/";

    @Bean
    RouterFunction<ServerResponse> authRouter(AuthController handler) {
        return RouterFunctions.route()
                .POST(PATH + "login", handler::login)
                .POST(PATH + "create", handler::create)
                .build();
    }
}
