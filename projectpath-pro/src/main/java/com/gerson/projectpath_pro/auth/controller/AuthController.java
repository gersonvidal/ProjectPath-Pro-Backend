package com.gerson.projectpath_pro.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gerson.projectpath_pro.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final String EMAIL_REGEX = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody final RegisterRequest request) {
        if (request.fullName() == null || request.fullName().isBlank()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.fullName().length() > 255) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.username() == null || request.username().isBlank()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.username().length() > 30) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.email() == null || request.email().isBlank()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!request.email().matches(EMAIL_REGEX)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.email().length() > 254) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.dateOfBirth() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String dateOfBirthString = request.dateOfBirth().toString();

        if (dateOfBirthString.isBlank()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.password() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.password().length() < 6) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final TokenResponse token = authService.register(request);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody final LoginRequest request) {
        if (request.email() == null || request.email().isBlank()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!request.email().matches(EMAIL_REGEX)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.email().length() > 254) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.password() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (request.password().length() < 6) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final TokenResponse token = authService.login(request);

        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public TokenResponse refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader) {
        return authService.refreshToken(authHeader);
    }
}