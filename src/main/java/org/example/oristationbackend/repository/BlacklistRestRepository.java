package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.BlacklistRest;
import org.example.oristationbackend.entity.BlacklistUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlacklistRestRepository extends JpaRepository<BlacklistRest, Integer> {
    Optional<BlacklistRest> findBlacklistUserByRestaurantRestId(int restId);

}
