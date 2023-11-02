package com.example.security.controllers;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Control the access of OpenAPI swagger endpoints if spring profile is 'prod'.
 */
@RestController
@Profile("prod")
public class SwaggerAccessController {

    @GetMapping(value =  "/swagger-ui/index.html")
    public ResponseEntity<String> swaggerRedirect() {
        return new ResponseEntity<>("Not Available", HttpStatus.NOT_FOUND);
    }

    @GetMapping(value =  "/v3/api-docs")
    public ResponseEntity<String> apiDocsRedirect() {
        return new ResponseEntity<>("Not Available", HttpStatus.NOT_FOUND);
    }

    @GetMapping(value =  "/v3/api-docs/swagger-config")
    public ResponseEntity<String> apiDocsSwaggerConfigRedirect() {
        return new ResponseEntity<>("Not Available", HttpStatus.NOT_FOUND);
    }
}
