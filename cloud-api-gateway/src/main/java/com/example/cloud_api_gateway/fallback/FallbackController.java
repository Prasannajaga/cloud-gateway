package com.example.cloud_api_gateway.fallback;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @RequestMapping("/fallback/users")
    public Mono<String> userFallback() {
        return Mono.just("User service is temporarily unavailable. Please try again later.");
    }
}
