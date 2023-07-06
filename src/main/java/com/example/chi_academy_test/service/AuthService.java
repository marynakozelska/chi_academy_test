package com.example.chi_academy_test.service;

import com.example.chi_academy_test.config.JwtUtil;
import com.example.chi_academy_test.dto.AuthRequest;
import com.example.chi_academy_test.exceptions.UserAlreadyExistsException;
import com.example.chi_academy_test.model.User;
import com.example.chi_academy_test.dto.AuthResponse;
import com.example.chi_academy_test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(AuthRequest request) {
        if (repository.existsByLogin(request.getLogin())) {
            throw new UserAlreadyExistsException("Sorry, this login is already taken, please, try again.");
        }

        var user = User.builder()
                .login(request.getLogin())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        repository.save(user);

        var jwtToken = jwtUtil.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(), request.getPassword()
                )
        );
        var user = repository.findByLogin(request.getLogin());
        var jwtToken = jwtUtil.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

}
