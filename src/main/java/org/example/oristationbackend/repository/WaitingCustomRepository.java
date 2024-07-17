package org.example.oristationbackend.repository;

import org.example.oristationbackend.dto.restaurant.WaitingRestResDto;
import org.example.oristationbackend.dto.user.WaitingResDto;

import java.util.List;
import java.util.Optional;

public interface WaitingCustomRepository {
    Optional<WaitingResDto> findByUserId(int userId);
    List<WaitingRestResDto> findByRestId(int restId);
    long countTodayByRestId(int restId);
    boolean existByUserIdAndDate(int userId);
}
