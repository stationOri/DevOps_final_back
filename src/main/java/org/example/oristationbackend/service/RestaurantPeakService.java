package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.restaurant.PeakListResDto;
import org.example.oristationbackend.dto.restaurant.PeakReqDto;
import org.example.oristationbackend.entity.RestaurantPeak;
import org.example.oristationbackend.exception.NoIdExistsException;
import org.example.oristationbackend.repository.RestaurantPeakRepository;
import org.example.oristationbackend.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantPeakService {
    private final RestaurantPeakRepository restaurantPeakRepository;
    private final RestaurantRepository restaurantRepository;

    //성수기
    public List<PeakListResDto> getPeakHolidays(int restId) {
        List<RestaurantPeak> peak = restaurantPeakRepository.findByRestaurant_RestId(restId);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (HH:mm)");

        return peak.stream()
                .map(restaurantPeak -> new PeakListResDto(
                        restaurantPeak.getPeakId(),
                        formatDate(restaurantPeak.getDateStart(), dateFormatter),
                        formatDate(restaurantPeak.getDateEnd(), dateFormatter),
                        formatTimestamp(restaurantPeak.getPeakOpendate(), timestampFormatter),
                        restaurantPeak.getRestaurant().getRestId()
                ))
                .collect(Collectors.toList());
    }

    private String formatDate(java.sql.Date sqlDate, DateTimeFormatter formatter) {
        return LocalDate.ofEpochDay(sqlDate.toLocalDate().toEpochDay()).format(formatter);
    }

    private String formatTimestamp(Timestamp timestamp, DateTimeFormatter formatter) {
        return timestamp.toLocalDateTime().format(formatter);
    }

    //삭제
    @Transactional
    public int deleteRestaurantPeak(int peakId) {
        IdExistCheck(peakId);
        int restId = restaurantPeakRepository.findById(peakId).get().getRestaurant().getRestId();
        restaurantPeakRepository.deleteById(peakId);
        return restId;
    }

    public void IdExistCheck(int tempId) {
        if(!restaurantPeakRepository.existsById(tempId)) {
            throw new NoIdExistsException("ID가 존재하지 않습니다.");
        }
    }


    @Transactional
    public int addRestaurantPeak(PeakReqDto peakReqDto) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        LocalDate startDate = LocalDate.parse(peakReqDto.getDateStart(), dateFormatter);
        LocalDate endDate = LocalDate.parse(peakReqDto.getDateEnd(), dateFormatter);

        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        LocalDateTime peakOpendate = LocalDateTime.parse(peakReqDto.getPeakOpendate(), timestampFormatter);
        Timestamp sqlPeakOpendate = Timestamp.valueOf(peakOpendate);

        RestaurantPeak restaurantPeak = new RestaurantPeak(
                0,
                sqlStartDate,
                sqlEndDate,
                sqlPeakOpendate,
                restaurantRepository.findById(peakReqDto.getRestId()).orElseThrow(() -> new RuntimeException("Restaurant not found"))
        );
        return restaurantPeakRepository.save(restaurantPeak).getPeakId();
    }
}
