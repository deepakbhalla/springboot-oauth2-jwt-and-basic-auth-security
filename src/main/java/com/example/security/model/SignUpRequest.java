package com.example.security.model;

import com.example.security.validator.PasswordMatches;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatches
public class SignUpRequest {

    @NotBlank(message = "username cannot be blank")
    private String username;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 6, message = "Minimum 6 characters required")
    private String password;

    @NotBlank(message = "matchingPassword cannot be blank")
    private String matchingPassword;

    public SignUpRequest(String username, String password, String matchingPassword) {
        this.username = username;
        this.password = password;
        this.matchingPassword = matchingPassword;
    }
}