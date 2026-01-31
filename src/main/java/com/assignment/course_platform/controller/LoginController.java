package com.assignment.course_platform.controller;

import com.assignment.course_platform.dto.requests.LoginRequestDto;
import com.assignment.course_platform.dto.responses.LoginResponseDto;
import com.assignment.course_platform.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody final LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(
                loginService.login(loginRequestDto)
        );
    }
}
