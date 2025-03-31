package com.example.testapp.model;

import com.example.testapp.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


/* Сущность пользователь */
@Entity
@Table(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private UserRole role;

    private boolean enabled;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verification_expiration")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime verificationCodeExpiresAt;

    @ElementCollection
    private List<Long> borrowedBook = new ArrayList<>();

    public User(Long id, String username, String email, String password, UserRole role, List<Long> borrowedBook) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.borrowedBook = borrowedBook;
    }

    public User() {
    }

    public User(String username, String email, String encode, UserRole role) {
        this.username = username;
        this.email = email;
        this.password = encode;
        this.role = role;
    }

    public User(String username, String email, String password, UserRole role, List<Long> borrowedBook) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.borrowedBook = borrowedBook;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public LocalDateTime getVerificationCodeExpiresAt() {
        return verificationCodeExpiresAt;
    }

    public void setVerificationCodeExpiresAt(LocalDateTime verificationCodeExpiresAt) {
        this.verificationCodeExpiresAt = verificationCodeExpiresAt;
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
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", enabled=" + enabled +
                ", verificationCode='" + verificationCode + '\'' +
                ", verificationCodeExpiresAt=" + verificationCodeExpiresAt +
                '}';
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            return List.of(new SimpleGrantedAuthority(UserRole.ROLE_ANONYMOUS.toString()));
        }
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @JsonProperty("authorities")
    public List<String> getAuthorityStrings() {
        return getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return enabled == user.enabled &&
                Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                role == user.role &&
                Objects.equals(verificationCode, user.verificationCode) &&
                Objects.equals(verificationCodeExpiresAt, user.verificationCodeExpiresAt) &&
                Objects.equals(borrowedBook, user.borrowedBook);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, role, enabled,
                verificationCode, verificationCodeExpiresAt, borrowedBook);
    }
}
