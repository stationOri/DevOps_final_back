package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.example.oristationbackend.dto.restaurant.WaitingRestResDto;
import org.example.oristationbackend.dto.user.SmsDto;
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
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WaitingService {
    private final SmsService smsService;
    private final WaitingRepository waitingRepository;
    private final RestaurantInfoRepository restaurantInfoRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    public List<WaitingRestResDto> getRestWaiting(int restId) {
        return waitingRepository.findByRestId(restId);
    }

    @Transactional(readOnly = false)
    public WaitingResDto getUserWaiting(int userId) {
        Optional<WaitingResDto> waiting = waitingRepository.findByUserId(userId);
        return waiting.orElse(null);
    }
    @Transactional(readOnly = false)
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
    @Transactional(readOnly = false)
    public int changeWaitingStatus(int waitingId, UserWaitingStatus userWaitingStatus) {
        Waiting waiting=waitingRepository.findById(waitingId).orElseThrow(() -> new IllegalArgumentException("waiting not found with id: " +waitingId));
        waiting=waiting.changeStatus(userWaitingStatus);
        if(userWaitingStatus==UserWaitingStatus.WALKIN_REQUESTED){
            String tonumber=waiting.getWaitingPhone();
            String username=waiting.getUser().getUserName();
            String restname=waiting.getRestaurant().getRestName();
            StringBuilder sb = new StringBuilder();
            sb.append("[WaitMate]\n ");
            sb.append(username);
            sb.append("고객님, 기다리느라 고생하셨어요!\n 지금 [");
            sb.append(restname);
            sb.append("]으로 입장해주세요. ");
            SmsDto smsDto=new SmsDto(tonumber,sb.toString());
            SingleMessageSentResponse resp=smsService.sendOne(smsDto);
            System.out.println(resp.getStatusMessage());
        }
        return waitingRepository.save(waiting).getWaitingId();
    }
}
