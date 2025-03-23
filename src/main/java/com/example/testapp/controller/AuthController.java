package com.example.testapp.controller;

import com.example.testapp.DTO.LoginUserDTO;
import com.example.testapp.DTO.RegisterUserDTO;
import com.example.testapp.DTO.VerifyUserDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("login")
    public String login(Model model) {
        model.addAttribute("loginUser", new LoginUserDTO());
        return "auth/login";
    }

    @GetMapping("signup")
    public String signup(Model model) {
        model.addAttribute("registerUser", new RegisterUserDTO());
        return "auth/signup";
    }

    @GetMapping("verify")
    public String verify(Model model, HttpSession session) {
        VerifyUserDTO verifyUserDTO = new VerifyUserDTO((String) session.getAttribute("email"),
                (String) session.getAttribute("password"));
        model.addAttribute("verifyUser", verifyUserDTO);
        return "auth/verify";
    }

    @GetMapping("resend")
    public String resend(Model model, HttpSession session) {
        model.addAttribute("email", session.getAttribute("email"));
        return "auth/verify";
    }
}
