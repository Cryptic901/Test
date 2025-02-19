package com.example.testapp.repository;

import com.example.testapp.model.Authors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/* Репозиторий для получения и передачи информации в базу данных об авторах */

@Repository
public interface AuthorsRepository extends JpaRepository<Authors, Long> {

    Optional<Authors> findById(long id);

}
