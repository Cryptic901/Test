package com.example.testapp.repository;

import com.example.testapp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/* Репозиторий для получения и передачи информации в базу данных о пользователях */

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findUsersByUsername(String username);

    Optional<Users> findByEmail(String email);
}
