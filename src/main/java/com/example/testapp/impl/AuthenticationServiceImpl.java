package com.example.testapp.impl;

import com.example.testapp.DTO.LoginUserDTO;
import com.example.testapp.DTO.RegisterUserDTO;
import com.example.testapp.DTO.VerifyUserDTO;
import com.example.testapp.exception.EntityNotFoundException;
import com.example.testapp.model.User;
import com.example.testapp.repository.UserRepository;
import com.example.testapp.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailServiceImpl emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtServiceImpl jwtService;


    public AuthenticationServiceImpl(UserRepository usersRepository,
                                     PasswordEncoder passwordEncoder,
                                     EmailServiceImpl emailService,
                                     AuthenticationManager authenticationManager,
                                     JwtServiceImpl jwtService) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public User signUp(RegisterUserDTO input) {
        User user = new User(input.getUsername(), input.getEmail(), passwordEncoder.encode(input.getPassword()), input.getRole());
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        sendVerificationEmail(user);
        return usersRepository.save(user);
    }

    public String authenticate(LoginUserDTO input) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        UserDetailsImpl userDetails = usersRepository.findByEmail(input.getEmail())
                .map(user -> new UserDetailsImpl(user.getEmail(), user.getPassword(), user.getAuthorities(), user.isEnabled()))
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + input.getEmail()));
        return jwtService.generateToken(userDetails);
    }

    public void verifyUser(VerifyUserDTO input) throws AuthenticationException {
        Optional<User> optionalUser = usersRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new AuthenticationException("Verification code expired");
            }
            if (user.getVerificationCode().equals(input.getVerificationCode())) {
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                user.setEnabled(true);
                usersRepository.save(user);
            } else {
                throw new AuthenticationException("Invalid verification code");
            }
        } else {
            throw new EntityNotFoundException("User with email " + input.getEmail() + " not found");
        }
    }

    public void resendVerificationCode(String email) throws AuthenticationException {
        Optional<User> optionalUser = usersRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new AuthenticationException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
            sendVerificationEmail(user);
            usersRepository.save(user);
        } else {
            throw new EntityNotFoundException("User with email " + email + " not found");
        }
    }

    public void sendVerificationEmail(User user) {
        String subject = "Verification Code";
        String verificationCode = user.getVerificationCode();
        String htmlMessage = "<!DOCTYPE html>"
                + "<html lang='en'>"
                + "<head>"
                + "<meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<title>Email Verification</title>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }"
                + ".container { max-width: 600px; background: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); }"
                + "h2 { color: #333; }"
                + "p { font-size: 16px; color: #555; }"
                + ".code { font-size: 20px; font-weight: bold; color: #007bff; background: #eef2ff; padding: 10px; display: inline-block; border-radius: 5px; }"
                + ".footer { margin-top: 20px; font-size: 14px; color: #777; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<h2>Verification Code</h2>"
                + "<p>Hello, " + user.getUsername() + "!</p>"
                + "<p>Thank you for signing up. Please use the following verification code to activate your account:</p>"
                + "<p class='code'>" + verificationCode + "</p>"
                + "<p>If you did not request this, please ignore this email.</p>"
                + "<p class='footer'>Best regards,<br>Library</p>"
                + "</div>"
                + "</body>"
                + "</html>";
        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
