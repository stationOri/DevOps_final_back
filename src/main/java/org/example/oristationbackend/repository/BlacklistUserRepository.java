package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.BlacklistUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistUserRepository extends JpaRepository<BlacklistUser, Integer> {
}
