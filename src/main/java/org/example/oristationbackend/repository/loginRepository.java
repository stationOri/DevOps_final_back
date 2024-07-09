package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Favorite;
import org.example.oristationbackend.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface loginRepository extends JpaRepository<Login, Integer> {
}
