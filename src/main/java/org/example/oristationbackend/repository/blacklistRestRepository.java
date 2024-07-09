package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Admin;
import org.example.oristationbackend.entity.BlacklistRest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface blacklistRestRepository extends JpaRepository<BlacklistRest, Integer> {
}
