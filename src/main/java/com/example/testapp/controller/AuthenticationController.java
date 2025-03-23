package com.example.testapp.controller;

import com.example.testapp.DTO.LoginUserDTO;
import com.example.testapp.DTO.RegisterUserDTO;
import com.example.testapp.DTO.VerifyUserDTO;
import com.example.testapp.model.User;
import com.example.testapp.responses.LoginResponse;
import com.example.testapp.impl.AuthenticationServiceImpl;
import com.example.testapp.impl.JwtServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
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
    public Object register(@ModelAttribute("registerUser") RegisterUserDTO registerUserDTO, HttpSession session) {
        User registeredUser = authenticationService.signUp(registerUserDTO);
        if (registeredUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        session.setAttribute("registerUser", registerUserDTO);
        session.setAttribute("email", registerUserDTO.getEmail());
        return "redirect:/verify";
    }

    @PostMapping("/login")
    @Operation(summary = "Залогиниться в аккаунт", description = "Вход пользователя в систему(разрешено только верифицированным пользователям)" +
            " и последующая выдача ему JWT токена который он должен отправлять с каждым запросом для получения доступа к API")
    public ResponseEntity<LoginResponse> authenticate(@ModelAttribute("loginUser") LoginUserDTO loginUserDTO) throws AuthenticationException {
        String jwtToken = authenticationService.authenticate(loginUserDTO);
        if (jwtToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    @Operation(summary = "Верифицировать аккаунт", description = "Верификация посредством введенного email'а, password'a" +
            " и выданного при регистрации на email верификационного кода")
    public ResponseEntity<?> verifyUser(@ModelAttribute("verifyUser") VerifyUserDTO verifyUserDTO) {
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
