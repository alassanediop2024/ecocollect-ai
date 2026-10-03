package com.ecocollect.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> home() {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("application", "EcoCollect AI API");
        response.put("status", "UP");
        response.put("version", "1.0");
        response.put("api", "/api");
        response.put("documentation", "/swagger-ui/index.html");
        response.put("health", "/actuator/health");
        return response;
    }
}
