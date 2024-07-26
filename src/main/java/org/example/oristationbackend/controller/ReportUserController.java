package org.example.oristationbackend.controller;
import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.admin.UserReportResDto;
import org.example.oristationbackend.dto.admin.UserReportStatusReqDto;
import org.example.oristationbackend.dto.restaurant.UserReportReqDto;
import org.example.oristationbackend.service.ReportUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userreport")
@RequiredArgsConstructor
public class ReportUserController {
    private final ReportUserService reportUserService;
    @PostMapping
    public int reportUser(@RequestBody UserReportReqDto userReportReqDto ) {
        return reportUserService.reportUser(userReportReqDto);
    }
    @PutMapping
    public int changeReportStatus(@RequestBody UserReportStatusReqDto userReportStatusReqDto){
        return reportUserService.changeReportStatus(userReportStatusReqDto);
    }

//    @GetMapping("/user/{userId}")
//    public List<UserReportResDto> getReportListByUser(@PathVariable("userId") int userId){
//        return reportUserService.getReportListByUser(userId);
//    }
//    @GetMapping("/status/{reportStatus}")
//    public List<UserReportResDto> getReportListByStatus(@PathVariable("reportStatus")ReportStatus status){
//        return reportUserService.getReportListByStatus(status);
//    }

    @GetMapping
    public List<UserReportResDto> getUserReportList(){
        List<UserReportResDto> list = reportUserService.getUserReportListAll();
        return list != null ? list : List.of();
    }
}
