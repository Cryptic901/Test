package com.example.testapp.controller;

import com.example.testapp.DTO.LoginUserDTO;
import com.example.testapp.DTO.RegisterUserDTO;
import com.example.testapp.DTO.VerifyUserDTO;
import com.example.testapp.impl.AuthenticationServiceImpl;
import com.example.testapp.impl.JwtServiceImpl;
import com.example.testapp.model.User;
import com.example.testapp.responses.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Аутентификация",
        description = "Методы для работы с данными аутентификации")
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;
    private final JwtServiceImpl jwtService;

    public AuthenticationController(AuthenticationServiceImpl authenticationService, JwtServiceImpl jwtService) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    @Operation(summary = "Зарегистрировать аккаунт", description = "Регистрация и добавление пользователя в БД," +
            " выдаётся код верификации, который нужно ввести по эндпоинту POST /auth/verify")
    public ResponseEntity<?> register(@RequestBody RegisterUserDTO registerUserDTO) {
        User registeredUser = authenticationService.signUp(registerUserDTO);
        if (registeredUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Enter valid data");
        }
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    @Operation(summary = "Залогиниться в аккаунт", description = "Вход пользователя в систему(разрешено только верифицированным пользователям)" +
            " и последующая выдача ему JWT токена который он должен отправлять с каждым запросом для получения доступа к API")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDTO loginUserDTO) {
        String jwtToken = authenticationService.authenticate(loginUserDTO);
        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not signed up");
        }
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    @Operation(summary = "Верифицировать аккаунт", description = "Верификация посредством введенного email'а, password'a" +
            " и выданного при регистрации на email верификационного кода")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDTO verifyUserDTO) {
        try {
            authenticationService.verifyUser(verifyUserDTO);
            return ResponseEntity.ok("Account verified successfully");
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    @Operation(summary = "Повторно отправить верификационный код на email", description = "Отправляет код на email" +
            " еще раз если пользователь его не получил по какой-либо причине")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code resent");
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
