package org.example.oristationbackend.controller;
import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.restaurant.WaitingRestResDto;
import org.example.oristationbackend.dto.user.WaitingReqDto;
import org.example.oristationbackend.dto.user.WaitingResDto;
import org.example.oristationbackend.entity.type.UserWaitingStatus;
import org.example.oristationbackend.service.WaitingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/waiting")
@RequiredArgsConstructor
public class WaitingController {
    private final WaitingService waitingService;
    @GetMapping("/rest/{restId}")
    public List<WaitingRestResDto> getRestWaiting(@PathVariable("restId") int restId) {
        return waitingService.getRestWaiting(restId);
    }
    @GetMapping("/user/{userId}")
    public WaitingResDto getUserWaiting(@PathVariable("userId") int userId) {
        return waitingService.getUserWaiting(userId);
    }
    @PostMapping
    public int addWaiting(@RequestBody WaitingReqDto waitingReqDto) {
        return waitingService.addWaiting(waitingReqDto);
        //0일 경우 식당이 웨이팅 받는 상태 아님
    }
    @PutMapping("/{waitingId}")
    public int changeWaitingStatus(@PathVariable("waitingId") int waitingId, @RequestBody UserWaitingStatus userWaitingStatus) {
        return waitingService.changeWaitingStatus(waitingId, userWaitingStatus);
    }
}
