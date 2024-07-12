package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.BlacklistUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlacklistUserRepository extends JpaRepository<BlacklistUser, Integer> {
    Optional<BlacklistUser> findBlacklistUserByUserUserId(int userId);
}
