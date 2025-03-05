package com.example.testapp.service;

import com.example.testapp.DTO.RegisterUserDTO;
import com.example.testapp.model.Users;

public interface AuthenticationService {
    Users signUp(RegisterUserDTO input);
    void sendVerificationEmail(Users user);
}
