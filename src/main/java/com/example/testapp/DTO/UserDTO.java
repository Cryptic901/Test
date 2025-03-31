package com.example.testapp.DTO;

import com.example.testapp.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* Объект для удобной передачи данных о пользователе */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO implements Serializable {

    @JsonIgnore
    private Long id;

    private String username;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private String password;

    @JsonProperty("role")
    private String role;

    @JsonIgnore
    private List<Long> borrowedBook = new ArrayList<>();

    public static UserDTO fromEntity(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole() != null ? user.getRole().name() : "ROLE_ANONYMOUS");
        dto.setBorrowedBook(user.getBorrowedBooks() != null
                ? new ArrayList<>(user.getBorrowedBooks()) : new ArrayList<>());
        return dto;
    }

    public UserDTO() {
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
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Long> getBorrowedBooks() {
        return borrowedBook;
    }

    public void setBorrowedBook(List<Long> borrowedBook) {
        if (this.borrowedBook == null) {
            this.borrowedBook = new ArrayList<>();
        }
        this.borrowedBook.clear();

        if (borrowedBook != null) {
            this.borrowedBook.addAll(borrowedBook);
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
                ", borrowedBook=" + borrowedBook +
                '}';
    }
}
