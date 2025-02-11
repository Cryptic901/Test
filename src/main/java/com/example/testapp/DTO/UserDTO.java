package com.example.testapp.DTO;

import com.example.testapp.model.Users;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* Объект для удобной передачи данных о пользователе */

public class UserDTO implements Serializable {

    private Long id;

    private String username;

    private String email;

    private String password;

    @JsonProperty("role")
    private String role;

    private List<Long> borrowedBooks;

    public static UserDTO fromEntity(Users user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setUserRole(user.getUserRole() != null ? user.getUserRole().name() : "UNDEFINED");
        dto.setBorrowedBooks(user.getBorrowedBooks() != null
                ? new ArrayList<>(user.getBorrowedBooks()) : new ArrayList<>());
        return dto;
    }

    public UserDTO() {
    }

    public UserDTO(Users user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getUserRole() != null ? user.getUserRole().toString() : "UNDEFINED";
        this.borrowedBooks = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("role")
    public String getUserRole() {
        return role;
    }

    public void setUserRole(String role) {
        this.role = role;
    }

    public List<Long> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<Long> borrowedBooks) {
        if (this.borrowedBooks == null) {
            this.borrowedBooks = new ArrayList<>();
        }
        this.borrowedBooks.clear();

        if (borrowedBooks != null) {
            this.borrowedBooks.addAll(borrowedBooks);
        }
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", borrowedBooks=" + borrowedBooks +
                '}';
    }
}
