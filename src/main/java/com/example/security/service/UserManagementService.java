package com.example.security.service;

import com.example.security.exception.ResourceNotFoundException;
import com.example.security.exception.UserAlreadyExistAuthenticationException;
import com.example.security.model.SignUpRequest;
import com.example.security.model.User;

import java.util.List;

public interface UserManagementService {

    List<User> getAllUsers();

    User registerNewUser(SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException;

    void deleteUser(String username) throws ResourceNotFoundException;
}
