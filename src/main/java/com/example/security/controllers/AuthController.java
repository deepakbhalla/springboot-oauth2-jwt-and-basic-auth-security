package com.example.security.controllers;

import com.example.security.entity.UserEntity;
import com.example.security.exception.UserAlreadyExistAuthenticationException;
import com.example.security.model.ApiGenericResponse;
import com.example.security.model.JwtAuthenticationResponse;
import com.example.security.model.SignUpRequest;
import com.example.security.model.User;
import com.example.security.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@Tag(name = "Authentication Management", description = "Authentication Management APIs")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserManagementService userManagementService;

    @Autowired
    JwtEncoder jwtEncoder;

    @Operation(summary = "Create a new user")
    @ApiResponse(responseCode = "200", description = "New user created successfully",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiGenericResponse.class))})
    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiGenericResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException {
        User user;
        try {
            user = this.userManagementService.registerNewUser(signUpRequest);
        } catch (UserAlreadyExistAuthenticationException e) {
            throw new UserAlreadyExistAuthenticationException("Username is already in use");
        }
        return ResponseEntity.ok().body(new ApiGenericResponse(true, "User registered successfully: " + user.getUsername()));
    }

    @PostMapping(value = "/token")
    public ResponseEntity<?> getToken(Authentication authentication) {
        UserEntity localUser = (UserEntity) authentication.getPrincipal();
        String jwt = getToken(localUser);
        return ResponseEntity.ok((new JwtAuthenticationResponse(jwt)));
    }

    private String getToken(UserEntity localUser) {
        Instant now = Instant.now();
        long expiry = 36000L;
        String scope = localUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(localUser.getUsername())
                .claim("scope", scope)
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
