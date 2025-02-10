package com.example.testapp.model;

import com.example.testapp.enums.UserRole;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


/* Сущность пользователь */
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ElementCollection
    private List<Long> borrowedBooks = new ArrayList<>();

    public Users(Long id, String username, String email, String password, UserRole role, List<Long> borrowedBooks) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.borrowedBooks = borrowedBooks;

    }

    public Users() {
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

    public UserRole getUserRole() {
        return role;
    }

    public void setUserRole(UserRole role) {
        this.role = role;
    }

    public List<Long> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<Long> borrowedBooks) {
        if(this.borrowedBooks == null) {
            this.borrowedBooks = new ArrayList<>();
        }
        this.borrowedBooks.clear();

        if(borrowedBooks != null) {
            this.borrowedBooks.addAll(borrowedBooks);
        }
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", borrowedBooks=" + borrowedBooks +
                '}';
    }
}
