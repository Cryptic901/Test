package com.example.testapp.impl;

import com.example.testapp.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

/* Сервис для работы с JWT токенами */

@Service
public class JwtServiceImpl implements JwtService {

    //Секретный ключ который помещается в токен
    @Value("${spring.security.jwt.secret-key}")
    private String secretKey;

    //Время истечения токена
    @Value("${spring.security.jwt.expiration-time}")
    private long jwtExpiration;

    // Метод для генерации ключа
    public SecretKey generateKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    //Метод для извлечения какой-то определенной части токена
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //Метод для генерации токена по данным пользователя
    public String generateToken(UserDetailsImpl userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }
    //Метод для генерации токена с дополнительными "претензиями" и временем истечения
    private String generateToken(HashMap<String, Object> extraClaims, UserDetailsImpl userDetails) {
    return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    //Метод для получения времени когда токен станет недействительным
    public long getExpirationTime() {
        return jwtExpiration;
    }
    //Метод для построения токена который закладывает в него роль,
    // username(по факту email), время генерации, время истечения и сгенерированный ключ
    private String buildToken(HashMap<String, Object> extraClaims,
                              UserDetailsImpl userDetails,
                              long jwtExpiration) {
        extraClaims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(generateKey())
                .compact();
    }

    //Метод для получения всех "претензий"
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //Метод для получения username из токена
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    //Метод для получения времени когда токен станет недействительным из токена
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    //Метод для получения роли из токена
    public String extractRole(String token) {
        return extractClaim(token, claims ->
                claims.get("role", String.class));
    }
    //Метод для валидации токена
    public boolean isTokenValid(String token, UserDetailsImpl userDetails) {
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
    //Метод для проверки того что срок действия токена не истёк
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    //Метод для проверки есть ли у пользователя такая роль
    public boolean hasRole(String token, String roleName) {
        String role = extractRole(token);
        return role.contains(roleName);
    }
}
