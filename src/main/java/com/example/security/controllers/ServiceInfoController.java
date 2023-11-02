package com.example.security.controllers;

import com.example.security.constant.AccountConstants;
import com.example.security.model.Account;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service information controller.
 */
@Tag(name = "Service Info Controller", description = "Service Information APIs")
@RestController
@RequestMapping(value = "/health")
public class ServiceInfoController {

    /**
     * Get service health check status.
     *
     * @return service status - String
     */
    @Operation(summary = "Get service health check status")
    @ApiResponse(responseCode = "200", description = "Found service status",
            content = {@Content(mediaType = "application/text", schema = @Schema(implementation = Account.class))})
    @GetMapping
    public ResponseEntity<String> info() {
        return new ResponseEntity<>(AccountConstants.SERVICE_IS_RUNNING.getMessage(), HttpStatus.OK);
    }
}
