package com.assignment.course_platform.service;

import com.assignment.course_platform.config.JwtConfig;
import com.assignment.course_platform.dto.requests.LoginRequestDto;
import com.assignment.course_platform.dto.responses.LoginResponseDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final JwtConfig jwtConfig;

    public LoginService(AuthenticationManager authenticationManager, JwtService jwtService, JwtConfig jwtConfig) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.jwtConfig = jwtConfig;
    }

    public LoginResponseDto login(final LoginRequestDto request) {
        final var authToken = UsernamePasswordAuthenticationToken
                .unauthenticated(request.email(), request.password());

        final var authentication = authenticationManager
                .authenticate(authToken);

        final var token = jwtService.generateToken(request.email());
        return new LoginResponseDto(
                token,
                request.email(),
                jwtConfig.getTtl().toSeconds());
    }
}
