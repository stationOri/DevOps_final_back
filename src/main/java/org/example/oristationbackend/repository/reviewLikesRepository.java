package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface reviewLikesRepository extends JpaRepository<Favorite, Integer> {
}
