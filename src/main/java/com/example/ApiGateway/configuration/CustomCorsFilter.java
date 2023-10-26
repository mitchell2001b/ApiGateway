package com.example.ApiGateway.configuration;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class CustomCorsFilter {

    @Bean
    public GlobalFilter corsFilter() {
        return new GlobalFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
                HttpHeaders responseHeaders = exchange.getResponse().getHeaders();

                responseHeaders.add("Access-Control-Allow-Origin", "*");
                responseHeaders.add("Access-Control-Allow-Methods", "GET, POST");
                responseHeaders.add("Access-Control-Allow-Headers", "Content-Type");
                responseHeaders.add("Access-Control-Allow-Credentials", "true");

                if (exchange.getRequest().getMethod() == null) {
                    return Mono.error(new IllegalArgumentException("No HTTP method"));
                }

                if (exchange.getRequest().getMethod().name().equals("OPTIONS")) {
                    exchange.getResponse().setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }

                return chain.filter(exchange);
            }
        };
    }
}