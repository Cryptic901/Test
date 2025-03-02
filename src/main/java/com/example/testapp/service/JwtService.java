package com.example.testapp.service;

import com.example.testapp.enums.UserRole;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public interface JwtService {

    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

    Claims extractAllClaims(String token);

    String extractUsername(String token);

    Date extractExpiration(String token);

    boolean isTokenExpired(String token);

    SecretKey generateKey();

    String extractRole(String token);

    boolean hasRole(String token, String roleName);
}
