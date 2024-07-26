package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.admin.RestReportListResDto;
import org.example.oristationbackend.dto.admin.RestReportStatusReqDto;
import org.example.oristationbackend.dto.user.RestReportReqDto;
import org.example.oristationbackend.entity.*;
import org.example.oristationbackend.entity.type.BlackStatus;
import org.example.oristationbackend.entity.type.ReportStatus;
import org.example.oristationbackend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportRestService {
    private final ReportRestRepository reportRestRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final BlacklistRestRepository blacklistRestRepository;

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
    @Transactional(readOnly = false)
    public int changeReportStatus(RestReportStatusReqDto changeDto) {
        Optional<ReportRest> optionalrrest= reportRestRepository.findById(changeDto.getRestReportId());
        ReportRest reportRest = optionalrrest.orElseThrow(() -> new IllegalArgumentException("Rest report not found with id: " + changeDto.getRestReportId()));
        Optional<Admin> optionaladmin= adminRepository.findById(changeDto.getAdminId());
        Admin admin= optionaladmin.orElseThrow(() -> new IllegalArgumentException("Admin not found with id: " + changeDto.getAdminId()));
        int id= reportRestRepository.save(reportRest.changeStatus(changeDto.getReportStatus(),admin)).getRestReportId();
        if(changeDto.getReportStatus()==ReportStatus.C){
            if(reportRestRepository.countByRestaurantRestIdAndReportStatus(reportRest.getRestaurant().getRestId(),ReportStatus.C)>2){
                Optional<BlacklistRest> optionalBlacklistRest=blacklistRestRepository.findBlacklistUserByRestaurantRestId(reportRest.getRestaurant().getRestId());
                if(optionalBlacklistRest.isPresent()){
                    BlacklistRest blacklistRest = optionalBlacklistRest.get();
                    blacklistRest=blacklistRest.addreport();
                    Restaurant restaurant=reportRest.getRestaurant();
                    restaurant=restaurant.changeblocked(true);
                    java.util.Date utilDate = new java.util.Date();
                    long milliSeconds = utilDate.getTime();
                    java.sql.Date sqlDate = new java.sql.Date(milliSeconds);
                    blacklistRest.setBanStartDate(sqlDate);
                    if(blacklistRest.getReportNum()>5){
                        restaurant.setRestIsopen(false);
                        restaurant.setQuitDate(sqlDate);
                        restaurant.setBlocked(true);
                        blacklistRest.changeStatus(BlackStatus.D);
                        restaurantRepository.save(restaurant);
                    }
                    blacklistRestRepository.save(blacklistRest);
                }
                else {
                    java.util.Date utilDate = new java.util.Date();
                    long milliSeconds = utilDate.getTime();
                    java.sql.Date sqlDate = new java.sql.Date(milliSeconds);
                    BlacklistRest blacklistRest = new BlacklistRest(0, new Timestamp(System.currentTimeMillis()),
                            BlackStatus.B, 3, sqlDate, reportRest.getRestaurant(), admin);
                    blacklistRestRepository.save(blacklistRest);
                }

            }

        }
        return id;
    }
}
