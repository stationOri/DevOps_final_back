package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByAdminPhone(String phone);
}
