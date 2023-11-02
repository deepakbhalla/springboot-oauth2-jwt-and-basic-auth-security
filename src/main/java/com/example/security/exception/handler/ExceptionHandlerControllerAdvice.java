package com.example.security.exception.handler;

import com.example.security.exception.ArgumentValidationException;
import com.example.security.exception.BadRequestException;
import com.example.security.exception.ExceptionResponse;
import com.example.security.exception.InsufficientAccountBalanceException;
import com.example.security.exception.ResourceNotFoundException;
import com.example.security.exception.UserAlreadyExistAuthenticationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ExceptionResponse handleMethodArgumentException(MethodArgumentNotValidException exception,
                                                             final HttpServletRequest request) throws JsonProcessingException {
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });

        return ExceptionResponse.builder()
                .invalidInputs(errorMap)
                .requestedURI(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

    }

    @ExceptionHandler(ArgumentValidationException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody ExceptionResponse handleArgumentValidationException(final ArgumentValidationException exception,
                                                                  final HttpServletRequest request) {

        return ExceptionResponse.builder()
                .errorMessage(exception.getMessage())
                .requestedURI(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody ExceptionResponse handleResourceNotFound(final ResourceNotFoundException exception,
                                                                  final HttpServletRequest request) {

        return ExceptionResponse.builder()
                .errorMessage(exception.getMessage())
                .requestedURI(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(UserAlreadyExistAuthenticationException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody ExceptionResponse handleUserAlreadyExistAuthenticationException(final UserAlreadyExistAuthenticationException exception,
                                                                  final HttpServletRequest request) {

        return ExceptionResponse.builder()
                .errorMessage(exception.getMessage())
                .requestedURI(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ExceptionResponse handleBadRequestException(final BadRequestException exception,
                                                                     final HttpServletRequest request) {

        return ExceptionResponse.builder()
                .errorMessage(exception.getMessage())
                .requestedURI(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ExceptionResponse handleHttpMediaTypeNotSupportedException(final HttpMediaTypeNotSupportedException exception,
                                                                                    final HttpServletRequest request) {

        return ExceptionResponse.builder()
                .errorMessage(exception.getMessage())
                .requestedURI(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ExceptionResponse handleMissingServletRequestParameterException(final MissingServletRequestParameterException exception,
                                                                                         final HttpServletRequest request) {

        return ExceptionResponse.builder()
                .errorMessage(exception.getMessage())
                .requestedURI(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(InsufficientAccountBalanceException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody ExceptionResponse handleInsufficientAccountBalanceException(final InsufficientAccountBalanceException exception,
                                                                                     final HttpServletRequest request) {

        return ExceptionResponse.builder()
                .errorMessage(exception.getMessage())
                .requestedURI(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ExceptionResponse handleException(final Exception exception,
                                                           final HttpServletRequest request) {

        return ExceptionResponse.builder()
                .errorMessage(exception.getMessage())
                .requestedURI(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
