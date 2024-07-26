package org.example.oristationbackend.controller;

import org.example.oristationbackend.dto.admin.RestReportListResDto;
import org.example.oristationbackend.dto.admin.RestReportStatusReqDto;
import org.example.oristationbackend.dto.user.RestReportReqDto;
import org.example.oristationbackend.service.ReportRestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/report")
public class ReportRestController {
    private final ReportRestService reportRestService;

    public ReportRestController(ReportRestService reportRestService) {
        this.reportRestService = reportRestService;
    }

    @GetMapping()
    public List<RestReportListResDto> getReport() {
        List<RestReportListResDto> reportList = reportRestService.getReportList();
        return reportList != null ? reportList : List.of();
    }
    @PostMapping()
    public int addReport(@RequestBody RestReportReqDto restReportReqDto) {
        return reportRestService.addReport(restReportReqDto);
    }
    @PutMapping()
    public int changeReportStatus( @RequestBody RestReportStatusReqDto changeDto) {
        return reportRestService.changeReportStatus(changeDto);
    }

}
