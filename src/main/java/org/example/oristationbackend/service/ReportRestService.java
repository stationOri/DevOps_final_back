package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.admin.RestReportListResDto;
import org.example.oristationbackend.dto.user.RestReportReqDto;
import org.example.oristationbackend.entity.ReportRest;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.User;
import org.example.oristationbackend.entity.type.ReportStatus;
import org.example.oristationbackend.repository.ReportRestRepository;
import org.example.oristationbackend.repository.RestaurantRepository;
import org.example.oristationbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportRestService {
    private final ReportRestRepository reportRestRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public List<RestReportListResDto> getReportList() {
        List<ReportRest> all = reportRestRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return all.stream()
                .map(rest -> {
                    String formattedDate = rest.getReportDate().toLocalDate().format(formatter);
                    return new RestReportListResDto(
                            rest.getRestReportId(),
                            rest.getRestaurant().getRestName(),
                            formattedDate,
                            rest.getReportContent(),
                            rest.getUser().getLogin().getEmail(),
                            rest.getReportStatus().getDescription(),
                            rest.getAdmin().getLogin().getEmail(),
                            rest.getRestaurant().getRestId()
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = false)
    public int addReport(RestReportReqDto restReportReqDto) {
        User user= userRepository.findById(restReportReqDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + restReportReqDto.getUserId()));
        Restaurant restaurant = restaurantRepository.findById(restReportReqDto.getRestId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with id: " + restReportReqDto.getRestId()));
        ReportRest reportRest = new ReportRest(0,restReportReqDto.getReportDate(),restReportReqDto.getReportContent(), ReportStatus.A,
                user,null,restaurant);
        return reportRestRepository.save(reportRest).getRestReportId();

    }
}
