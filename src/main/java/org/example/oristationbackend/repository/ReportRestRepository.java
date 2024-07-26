package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.ReportRest;
import org.example.oristationbackend.entity.type.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRestRepository extends JpaRepository<ReportRest, Integer> {
    int countByRestaurantRestIdAndReportStatus(int restId, ReportStatus reportStatus);

}
