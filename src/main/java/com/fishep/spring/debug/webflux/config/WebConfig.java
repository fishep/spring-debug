package com.fishep.spring.debug.webflux.config;

import com.fishep.spring.debug.webflux.ws.Handler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.config.PathMatchConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebConfig implements WebFluxConfigurer {

    @Override
    public void configurePathMatching(PathMatchConfigurer configurer) {

        configurer.addPathPrefix("/api", clazz -> clazz.isAnnotationPresent(RestController.class));

        WebFluxConfigurer.super.configurePathMatching(configurer);
    }

    @Bean
    public HandlerMapping handlerMapping(Handler handler) {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/ws/handler", handler);
        int order = -1; // before annotated controllers
        return new SimpleUrlHandlerMapping(map, order);
    }

//    @Override
//    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
//        configurer.defaultCodecs().enableLoggingRequestDetails(true);
//        WebFluxConfigurer.super.configureHttpMessageCodecs(configurer);
//    }

}
