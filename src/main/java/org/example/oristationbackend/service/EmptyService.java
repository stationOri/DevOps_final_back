package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.user.VacantApplyReqDto;
import org.example.oristationbackend.entity.Empty;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.User;
import org.example.oristationbackend.repository.EmptyRepository;
import org.example.oristationbackend.repository.RestaurantRepository;
import org.example.oristationbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmptyService {
    private final EmptyRepository emptyRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    // 빈자리 알림 신청 (statsu = false)
    @Transactional
    public void applyVacant(VacantApplyReqDto vacantApplyReqDto) {
      User user = userRepository.findById(vacantApplyReqDto.getUserId())
          .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

      Restaurant restaurant = restaurantRepository.findById(vacantApplyReqDto.getRestId())
          .orElseThrow(() -> new IllegalArgumentException("식당을 찾을 수 없습니다."));

      LocalDate targetDate = LocalDate.parse(vacantApplyReqDto.getDate());
      LocalTime targetTime = LocalTime.parse(vacantApplyReqDto.getTime());

      Date sqlDate = Date.valueOf(targetDate);
      Time sqlTime = Time.valueOf(targetTime);

      Empty empty = new Empty();
      empty.setUser(user);
      empty.setRestaurant(restaurant);
      empty.setDate(sqlDate);
      empty.setTime(sqlTime);
      empty.setPeople(vacantApplyReqDto.getPeople());
      empty.setStatus(false);

      emptyRepository.save(empty);
    }
}

