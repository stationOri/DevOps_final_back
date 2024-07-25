package org.example.oristationbackend.controller;

import org.example.oristationbackend.dto.admin.RestReportListResDto;
import org.example.oristationbackend.service.ReportRestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
