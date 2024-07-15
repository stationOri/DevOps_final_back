package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    List<Favorite> findByUser_UserId(int userId);
}
