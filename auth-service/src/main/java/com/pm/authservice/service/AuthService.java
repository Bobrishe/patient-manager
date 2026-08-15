package com.pm.authservice.service;

import com.pm.authservice.Util.JwtUtil;
import com.pm.authservice.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Optional<String> authenticate(LoginRequestDto loginDto) {
        return userService
                .findByEmail(loginDto.email())
                .filter(u -> passwordEncoder.matches(loginDto.password(), u.getPassword()))
                .map(u -> jwtUtil.generateToken(u.getEmail(), u.getRole()));
    }

}
