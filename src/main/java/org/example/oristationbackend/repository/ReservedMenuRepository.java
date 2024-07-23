package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.ReservedMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservedMenuRepository extends JpaRepository<ReservedMenu, Integer> {
    List<ReservedMenu> findByReservation_ResId(int resId);
}
