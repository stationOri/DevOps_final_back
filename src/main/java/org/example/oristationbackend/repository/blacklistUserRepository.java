package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.BlacklistRest;
import org.example.oristationbackend.entity.BlacklistUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface blacklistUserRepository extends JpaRepository<BlacklistUser, Integer> {
}
