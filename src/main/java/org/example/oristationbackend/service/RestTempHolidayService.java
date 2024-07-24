package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.restaurant.TempHolidayReqDto;
import org.example.oristationbackend.dto.restaurant.TempHolidayResDto;
import org.example.oristationbackend.entity.RestTempHoliday;
import org.example.oristationbackend.exception.NoIdExistsException;
import org.example.oristationbackend.repository.RestTempHolidayRepository;
import org.example.oristationbackend.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestTempHolidayService {
    private final RestTempHolidayRepository restTempHolidayRepository;
    private final RestaurantRepository restaurantRepository;

    public List<TempHolidayResDto> getTempHolidays(int restId) {
        List<RestTempHoliday> restTempHolidays = restTempHolidayRepository.findByRestaurant_RestId(restId);

        // 날짜 포맷터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return restTempHolidays.stream()
                .map(restTempHoliday -> new TempHolidayResDto(
                        restTempHoliday.getTempHolidayId(),
                        formatDate(restTempHoliday.getStartDate(), formatter),
                        formatDate(restTempHoliday.getEndDate(), formatter),
                        restTempHoliday.getRestaurant().getRestId()
                ))
                .collect(Collectors.toList());
    }

    // 날짜를 문자열로 변환하는 메서드
    private String formatDate(java.sql.Date sqlDate, DateTimeFormatter formatter) {
        return LocalDate.ofEpochDay(sqlDate.toLocalDate().toEpochDay()).format(formatter);
    }

    //삭제
    @Transactional
    public int deleteTempHoliday(int tempId) {
        IdExistCheck(tempId);
        int restId = restTempHolidayRepository.findById(tempId).get().getRestaurant().getRestId();
        restTempHolidayRepository.deleteById(tempId);
        return restId;
    }

    public void IdExistCheck(int tempId) {
        if(!restTempHolidayRepository.existsById(tempId)) {
            throw new NoIdExistsException("ID가 존재하지 않습니다.");
        }
    }
    //임시휴무 추가
    @Transactional
    public int addTempholiday(TempHolidayReqDto tempHolidayReqDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startDate = LocalDate.parse(tempHolidayReqDto.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(tempHolidayReqDto.getEndDate(), formatter);

        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        // RestTempHoliday 객체 생성
        RestTempHoliday holiday = new RestTempHoliday(
                0,
                sqlStartDate,
                sqlEndDate,
                restaurantRepository.findById(tempHolidayReqDto.getRestId()).get()
        );

        return restTempHolidayRepository.save(holiday).getTempHolidayId();
    }
}
