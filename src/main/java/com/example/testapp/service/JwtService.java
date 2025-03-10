package com.example.testapp.service;

import com.example.testapp.service.impl.UserDetailsImpl;
import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;
import java.util.Date;

public interface JwtService {

    String generateToken(UserDetailsImpl userDetails);

    boolean isTokenValid(String token, UserDetailsImpl userDetails);

    Claims extractAllClaims(String token);

    String extractUsername(String token);

    Date extractExpiration(String token);

    boolean isTokenExpired(String token);

    SecretKey generateKey();

    String extractRole(String token);

    boolean hasRole(String token, String roleName);
}
