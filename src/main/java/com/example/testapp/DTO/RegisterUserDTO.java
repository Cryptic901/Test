package com.example.testapp.DTO;

import com.example.testapp.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class RegisterUserDTO {

    private String username;
    private String password;
    private String email;
    @JsonIgnore
    private UserRole role = UserRole.ROLE_USER;

    public RegisterUserDTO() {}

    public RegisterUserDTO(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
