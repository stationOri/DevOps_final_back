package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Favorite;
import org.example.oristationbackend.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface favoriteRepository extends JpaRepository<Favorite, Integer> {
}
