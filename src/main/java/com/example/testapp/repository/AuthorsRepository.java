package com.example.testapp.repository;

import com.example.testapp.model.Authors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/* Репозиторий для получения и передачи информации в базу данных о авторах */

@Repository
public interface AuthorsRepository extends JpaRepository<Authors, Long> {

    Authors findById(long id);

}
