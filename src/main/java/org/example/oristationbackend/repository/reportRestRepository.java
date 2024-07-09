package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Payment;
import org.example.oristationbackend.entity.ReportRest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface reportRestRepository extends JpaRepository<ReportRest, Integer> {
}
