package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.ReportRest;
import org.example.oristationbackend.entity.ReportUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface reportUserRepository extends JpaRepository<ReportUser, Integer> {
}
