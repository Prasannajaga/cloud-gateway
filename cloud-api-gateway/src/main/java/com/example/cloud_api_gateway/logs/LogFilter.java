//package com.example.cloud_api_gateway.logs;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//
//
//@Component
//public class LogFilter implements GatewayFilterFactory {
//
//    private static final Logger log = LoggerFactory.getLogger(LogFilter.class);
//
//
//    @Override
//    public GatewayFilter apply(Object config) {
//        return (exchange, chain) -> {
//            ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();
//            log.info("[Gateway][Request] Method: {}, Path: {}", request.getMethod(), request.getURI().getPath());
//
//            return chain.filter(exchange)
//                    .doOnError(e -> log.error("[Gateway][Error] URI: {}, Error: {}", request.getURI(), e.getMessage()));
//        };
//    }
//}
//
//
