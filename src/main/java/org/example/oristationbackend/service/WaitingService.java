package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.restaurant.WaitingRestResDto;
import org.example.oristationbackend.dto.user.WaitingReqDto;
import org.example.oristationbackend.dto.user.WaitingResDto;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.RestaurantInfo;
import org.example.oristationbackend.entity.User;
import org.example.oristationbackend.entity.Waiting;
import org.example.oristationbackend.entity.type.ReservationType;
import org.example.oristationbackend.entity.type.RestWatingStatus;
import org.example.oristationbackend.entity.type.UserWaitingStatus;
import org.example.oristationbackend.repository.RestaurantInfoRepository;
import org.example.oristationbackend.repository.RestaurantRepository;
import org.example.oristationbackend.repository.UserRepository;
import org.example.oristationbackend.repository.WaitingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final RestaurantInfoRepository restaurantInfoRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    public List<WaitingRestResDto> getRestWaiting(int restId) {
        return waitingRepository.findByRestId(restId);
    }

    public WaitingResDto getUserWaiting(int userId) {
        if(waitingRepository.findByUserId(userId).isPresent()){
            return waitingRepository.findByUserId(userId).get();
        };return null;
    }

    public int addWaiting(WaitingReqDto waitingReqDto) {
        if(waitingRepository.existByUserIdAndDate(waitingReqDto.getUserId())){
            return -1;
        }
        if(!checkWaiting(waitingReqDto.getRestId())){
            return 0;
        }
        User user= userRepository.findById(waitingReqDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("user not found with id: " +waitingReqDto.getUserId()));
        Restaurant restaurant = restaurantRepository.findById(waitingReqDto.getRestId()).orElseThrow(() -> new IllegalArgumentException("Restaurant not found with id: " +waitingReqDto.getRestId()));
        long count= waitingRepository.countTodayByRestId(waitingReqDto.getRestId());
        Waiting waiting= new Waiting(0,user,restaurant, (int) count+1,new Timestamp(System.currentTimeMillis()),waitingReqDto.getWatingPhone(),user.getUserName(),waitingReqDto.getWaitingPpl(), UserWaitingStatus.IN_QUEUE);
        return waitingRepository.save(waiting).getWaitingId();
    }

    private boolean checkWaiting(int restId){
        RestaurantInfo restaurantInfo = restaurantInfoRepository.findById(restId).orElseThrow(() -> new IllegalArgumentException("restaurantInfo not found with id: " +restId));
        if(restaurantInfo.getRevWait()== ReservationType.B){
            return false;
        }
        return restaurantInfo.getRestWaitingStatus() == RestWatingStatus.C;
    }

    public int changeWaitingStatus(int waitingId, UserWaitingStatus userWaitingStatus) {
        Waiting waiting=waitingRepository.findById(waitingId).orElseThrow(() -> new IllegalArgumentException("waiting not found with id: " +waitingId));
        waiting=waiting.changeStatus(userWaitingStatus);
        //만약 userwaitingstatus가 입장요청이면 알림톡 보내기
        return waitingRepository.save(waiting).getWaitingId();
    }
}
