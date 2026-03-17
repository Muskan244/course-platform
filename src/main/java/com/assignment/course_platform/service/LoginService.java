package com.assignment.course_platform.service;

import com.assignment.course_platform.config.JwtConfig;
import com.assignment.course_platform.dto.requests.LoginRequestDto;
import com.assignment.course_platform.dto.responses.LoginResponseDto;
import com.assignment.course_platform.exception.ResourceNotFoundException;
import com.assignment.course_platform.model.User;
import com.assignment.course_platform.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final JwtConfig jwtConfig;

    private final UserRepository userRepository;

    public LoginService(AuthenticationManager authenticationManager, JwtService jwtService, JwtConfig jwtConfig, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.jwtConfig = jwtConfig;
        this.userRepository = userRepository;
    }

    public LoginResponseDto login(final LoginRequestDto request) throws ResourceNotFoundException {
        final var authToken = UsernamePasswordAuthenticationToken
                .unauthenticated(request.email(), request.password());

        final var authentication = authenticationManager
                .authenticate(authToken);

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        final var role = user.getRole();

        final var token = jwtService.generateToken(request.email(), role);
        return new LoginResponseDto(
                token,
                request.email(),
                jwtConfig.getTtl().toSeconds());
    }
}
