package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Reservation;
import org.example.oristationbackend.entity.ReservedMenu;
import org.example.oristationbackend.entity.type.ReservationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface reservedMenuRepository extends JpaRepository<ReservedMenu, Integer> {
}
