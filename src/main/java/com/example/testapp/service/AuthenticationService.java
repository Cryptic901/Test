package com.example.testapp.service;

import com.example.testapp.DTO.RegisterUserDTO;
import com.example.testapp.model.User;

public interface AuthenticationService {
    User signUp(RegisterUserDTO input);
    void sendVerificationEmail(User user);
}
