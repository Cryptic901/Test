package com.example.testapp.controller;

import com.example.testapp.DTO.LoginUserDTO;
import com.example.testapp.DTO.RegisterUserDTO;
import com.example.testapp.DTO.VerifyUserDTO;
import com.example.testapp.SetUpForIntegrationTests;
import com.example.testapp.exception.EntityNotFoundException;
import com.example.testapp.model.User;
import com.example.testapp.responses.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest extends SetUpForIntegrationTests {

    @Autowired
    private MockMvc mvc;


    @Test
    void register_success() throws Exception {
        User signUpedUser = registerUser("User№4", "password4",
                "user4@gmail.com");
        assertThat(signUpedUser.getVerificationCode()).isNotNull();
        assertThat(signUpedUser.getVerificationCodeExpiresAt()).isNotNull();
    }

    @Test
    void verify_success() throws Exception {
        User signUpedUser = registerUser("User№4", "password4",
                "user4@gmail.com");

        verifyUser(signUpedUser.getEmail(), "password4", signUpedUser.getVerificationCode());
        User updatedUser = userRepository.findUserByUsername(signUpedUser.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        assertThat(updatedUser.isEnabled()).isTrue();
        assertThat(updatedUser.getVerificationCode()).isNull();
        assertThat(updatedUser.getVerificationCodeExpiresAt()).isNull();
    }

    @Test
    void resend_success() throws Exception {
        User signUpedUser = registerUser("User№4", "password4",
                "user4@gmail.com");
        String oldVerificationCode = signUpedUser.getVerificationCode();

        mvc.perform(post("/auth/resend")
                        .param("email", signUpedUser.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().string("Verification code resent"));

        User updatedUser = userRepository.findUserByUsername(signUpedUser.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        assertThat(oldVerificationCode).isNotEqualTo(updatedUser.getVerificationCode());
    }

    @Test
    void login_success() throws Exception {
        User signUpedUser = registerUser("User№4", "password4",
                "user4@gmail.com");

        verifyUser(signUpedUser.getEmail(), "password4", signUpedUser.getVerificationCode());
        User updatedUser = userRepository.findUserByUsername(signUpedUser.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        assertThat(updatedUser.isEnabled()).isTrue();
        LoginResponse loginResponse = loginUser(updatedUser.getEmail(), "password4");
        assertThat(loginResponse.getToken()).isNotNull();
        assertThat(loginResponse.getExpiresIn()).isNotNull();
    }

    private User registerUser(String username, String password, String email) throws Exception {
        RegisterUserDTO dto = new RegisterUserDTO(username, password, email);
        String jsonRequestRegister = mapper.writeValueAsString(dto);

        var result = mvc.perform(post("/auth/signup")
                        .content(jsonRequestRegister)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        return mapper.readValue(result.getResponse().getContentAsString(), User.class);
    }

    private void verifyUser(String email, String password, String verificationCode) throws Exception {
        VerifyUserDTO verifyUserDTO = new VerifyUserDTO(email, password, verificationCode);
        String jsonRequestVerify = mapper.writeValueAsString(verifyUserDTO);

        mvc.perform(post("/auth/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequestVerify))
                .andExpect(status().isOk())
                .andExpect(content().string("Account verified successfully"));
    }

    private LoginResponse loginUser(String email, String password) throws Exception {
        LoginUserDTO loginUserDTO = new LoginUserDTO(email, password);
        String jsonRequestLogin = mapper.writeValueAsString(loginUserDTO);

      var result = mvc.perform(post("/auth/login")
                        .content(jsonRequestLogin)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        return mapper.readValue(result.getResponse().getContentAsString(), LoginResponse.class);
    }
}
