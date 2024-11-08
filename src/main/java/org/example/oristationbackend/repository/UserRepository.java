package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserPhone(String phone);
    boolean existsByUserPhone(String phone);
}
