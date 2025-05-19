package com.example.cloud_api_gateway.logs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogService {

    public void logSomething() {
        log.info("Logging using Lombok Slf4j");
    }
}