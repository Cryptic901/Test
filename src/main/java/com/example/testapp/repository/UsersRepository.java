package com.example.testapp.repository;

import com.example.testapp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/* Репозиторий для получения и передачи информации в базу данных о пользователях */

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findById(long id);
    Users findByUsername(String username);

}
