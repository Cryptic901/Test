package com.example.testapp.repository;

import com.example.testapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/* Репозиторий для получения и передачи информации в базу данных о пользователях */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    Optional<User> findByEmail(String email);

}
