package kz.halykacademy.bookstore.services.impl;

import kz.halykacademy.bookstore.dto.auth.AuthenticationRequest;
import kz.halykacademy.bookstore.dto.auth.AuthenticationResponse;
import kz.halykacademy.bookstore.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl {
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity login(AuthenticationRequest request) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword());

            try {
                authenticationManager.authenticate(authToken);
            } catch (BadCredentialsException e) {
                throw new BadCredentialsException(e.getMessage());
            }

            String token = jwtUtil.generateToken(request.getLogin());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new AuthenticationResponse(token));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
