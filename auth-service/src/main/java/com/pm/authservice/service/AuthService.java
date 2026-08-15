package com.pm.authservice.service;

import com.pm.authservice.Util.JwtUtil;
import com.pm.authservice.dto.LoginRequestDto;
import io.jsonwebtoken.JwtException;
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

    public boolean validateToken(String token) {
        try {
            jwtUtil.validateToken(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }

    }

}
