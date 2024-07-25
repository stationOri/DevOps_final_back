package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.controller.ReportUserController;
import org.example.oristationbackend.dto.admin.RestReportIdResDto;
import org.example.oristationbackend.dto.admin.RestReportListResDto;
import org.example.oristationbackend.dto.admin.UserReportResDto;
import org.example.oristationbackend.dto.admin.UserReportStatusReqDto;
import org.example.oristationbackend.dto.restaurant.ReviewInfoDto;
import org.example.oristationbackend.dto.restaurant.UserReportReqDto;
import org.example.oristationbackend.entity.*;
import org.example.oristationbackend.entity.type.BlackStatus;
import org.example.oristationbackend.entity.type.ReportStatus;
import org.example.oristationbackend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportUserService {
    private final ReportUserRepository reportUserRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final AdminRepository adminRepository;
    private final BlacklistUserRepository blacklistUserRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = false)
    public int reportUser(UserReportReqDto userReportReqDto){
        try {
                Review review = reviewRepository.findById(userReportReqDto.getReviewId()).get();
                int restId = review.getRestaurant().getRestId();
                int userId = review.getUser().getUserId();

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " +userId));

            Restaurant restaurant = restaurantRepository.findById(restId)
                    .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with id: " + restId));

            ReportUser ruser = new ReportUser(0,
                    userReportReqDto.getReportDate(),
                    userReportReqDto.getReportContent(),
                    ReportStatus.A,
                    user,
                    null,
                    restaurant,review);

            return reportUserRepository.save(ruser).getUserReportId();
        } catch (IllegalArgumentException e) {
            // 예외 처리 로직
            e.printStackTrace(); // 예외 메시지 출력
            throw e; // 혹은 다른 예외를 던지거나 적절히 처리
        }

    }
    @Transactional(readOnly = false)
    public int changeReportStatus(UserReportStatusReqDto changeDto){
        Optional<ReportUser> optionalruser= reportUserRepository.findById(changeDto.getUserReportId());
        ReportUser ruser = optionalruser.orElseThrow(() -> new IllegalArgumentException("User report not found with id: " + changeDto.getUserReportId()));
        Optional<Admin> optionaladmin= adminRepository.findById(changeDto.getAdminId());
        Admin admin= optionaladmin.orElseThrow(() -> new IllegalArgumentException("Admin not found with id: " + changeDto.getAdminId()));
        int id= reportUserRepository.save(ruser.changeStatus(changeDto.getReportStatus(),admin)).getUserReportId();

        //신고 3회 이상시 블랙리스트에 올리기
        if(changeDto.getReportStatus()==ReportStatus.C){
            //신고받은 리뷰 블라인드하기
            Review review=ruser.getReview();
            reviewRepository.save(review.blindReview());
            int userId=ruser.getUser().getUserId();
            if(reportUserRepository.countByUserUserIdAndReportStatus(userId,ReportStatus.C)>2){
                //신고 3회 부터, 이미 블랙리스트에 있는지 확인, 있으면 누적 신고 +1
                //없으면 블랙리스트에 올리기
                Optional<BlacklistUser> optionalBlacklistUser = blacklistUserRepository.findBlacklistUserByUserUserId(userId);
                if (optionalBlacklistUser.isPresent()) {
                    BlacklistUser blacklistUser = optionalBlacklistUser.get();
                    blacklistUser= blacklistUser.addreport();
                    if(blacklistUser.getReportNum()>5){
                        blacklistUser.changeStatus(BlackStatus.D);
                        quitUser(userId);
                    }
                    blacklistUserRepository.save(blacklistUser);
                }else{
                    java.util.Date utilDate = new java.util.Date();
                    long milliSeconds = utilDate.getTime();
                    java.sql.Date sqlDate = new java.sql.Date(milliSeconds);
                    BlacklistUser blacklistUser=new BlacklistUser(0,new Timestamp(System.currentTimeMillis()),
                            BlackStatus.B,3, sqlDate,ruser.getUser(),admin);
                    User user= ruser.getUser();
                    userRepository.save(user);
                    blacklistUserRepository.save(blacklistUser);
                }
            }
        }
        return id;
    }

    private void quitUser(int userId){
        Optional<User> optionaluser=userRepository.findById(userId);
        User user = optionaluser.orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        userRepository.save(user.quitUser());
    }
//    public List<UserReportResDto> getReportListByUser(int userId){
//        return reportUserRepository.findReportUserByUserId(userId);
//    }
//    public List<UserReportResDto> getReportListByStatus(ReportStatus status){
//        return reportUserRepository.findReportUserByStatus(status);
//    }

    public List<UserReportResDto> getUserReportListAll() {
        List<ReportUser> reportUserList = reportUserRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return  reportUserList.stream()

                .map(user->{
                    String formattedDate = user.getReportDate().toLocalDate().format(formatter);
                    return new UserReportResDto(
                            user.getUserReportId(),
                            user.getUser().getUserName(),
                            formattedDate,
                            user.getReview().getReviewData(),
                            user.getReportContent(),
                            user.getRestaurant().getLogin().getEmail(),
                            user.getReportStatus().getDescription(),
                            user.getAdmin().getLogin().getEmail()
                    );
                })
                .collect(Collectors.toList());

    }


}
