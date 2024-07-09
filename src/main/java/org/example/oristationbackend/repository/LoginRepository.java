package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<Login, Integer> {
}
