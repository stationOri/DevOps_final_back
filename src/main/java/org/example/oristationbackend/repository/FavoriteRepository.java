package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Favorite;
import org.example.oristationbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    List<Favorite> findByUser_UserId(int userId);
    List<Favorite> findByUser(User user);
    void deleteByUser_UserIdAndRestaurant_RestId(int userId, int restaurantId);
}
