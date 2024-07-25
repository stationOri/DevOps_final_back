package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.admin.RestReportListResDto;
import org.example.oristationbackend.entity.ReportRest;
import org.example.oristationbackend.repository.ReportRestRepository;
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

}
