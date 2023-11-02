package com.example.security.service.impl;

import com.example.security.constant.AccountConstants;
import com.example.security.deserializer.UserDetailsDeserializer;
import com.example.security.entity.UserEntity;
import com.example.security.exception.ResourceNotFoundException;
import com.example.security.exception.UserAlreadyExistAuthenticationException;
import com.example.security.model.SignUpRequest;
import com.example.security.model.User;
import com.example.security.repository.UserRepository;
import com.example.security.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDetailsDeserializer userDetailsDeserializer;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return this.userDetailsDeserializer.deserializeAccount(this.userRepository.findAll());
    }

    @Override
    @Transactional(value = "transactionManager")
    public User registerNewUser(final SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException {
        if (this.userRepository.existsByUsernameIgnoreCase(signUpRequest.getUsername())) {
            throw new UserAlreadyExistAuthenticationException("Username '" + signUpRequest.getUsername() + "' already exists");
        }
        UserEntity user = buildUser(signUpRequest);
        user = this.userRepository.save(user);
        this.userRepository.flush();
        return User.builder().username(user.getUsername()).password(user.getPassword()).build();
    }

    @Override
    @Transactional(value = "transactionManager")
    public void deleteUser(String username) throws ResourceNotFoundException {
        if (!this.userRepository.existsByUsernameIgnoreCase(username)) {
            throw new ResourceNotFoundException(AccountConstants.USER_NOT_FOUND.getMessage());
        }
        this.userRepository.deleteByUsername(username);
        this.userRepository.flush();
    }

    private UserEntity buildUser(final SignUpRequest signUpRequest) {
        return UserEntity.builder()
                .username(signUpRequest.getUsername())
                .password(this.passwordEncoder.encode(signUpRequest.getPassword()))
                .build();
    }
}
