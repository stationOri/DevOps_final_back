package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Reservation;
import org.example.oristationbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<User, Integer> {
}
