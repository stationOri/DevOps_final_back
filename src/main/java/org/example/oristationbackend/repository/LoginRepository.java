package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginRepository extends JpaRepository<Login, Integer> {

    Optional<Login> findByEmail(String email);
}
