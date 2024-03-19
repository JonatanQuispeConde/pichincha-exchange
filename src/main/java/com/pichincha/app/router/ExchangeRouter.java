package com.pichincha.app.router;

import com.pichincha.app.controller.ExchangeController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Slf4j
public class ExchangeRouter {

    private static final String PATH = "api/v1/exchange";
    
    @Bean
    RouterFunction<ServerResponse> routerExchange(ExchangeController handler) {
        return RouterFunctions.route()
                .GET(PATH, handler::getAll)
                .GET(PATH+"/logs", handler::getLogsAll)
                .GET(PATH + "/{id}", handler::getOneById)
                .POST(PATH, handler::save)
                .POST(PATH+"/all", handler::saveAll)
                .POST(PATH+"/change-money", handler::changeMoney)
                .PUT(PATH + "/{id}", handler::update)
                .DELETE(PATH + "/{id}", handler::delete)
                .build();
    }
}
