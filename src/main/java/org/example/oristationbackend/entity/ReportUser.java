package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.oristationbackend.entity.type.ReportStatus;

import java.sql.Date;

@Entity
@Getter
@Table(name = "report_user")
@AllArgsConstructor
@NoArgsConstructor
public class ReportUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int userReportId;

  @Column(name = "report_date", nullable = false)
  private Date reportDate;

  @Column(name = "report_content", nullable = false, length = 200)
  private String reportContent;

  @Enumerated(EnumType.STRING)
  @Column(name = "report_status", nullable = false, length = 1)
  private ReportStatus reportStatus;

//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "user_id")
//  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "admin_id")
  private Admin admin;

//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "rest_id")
//  private Restaurant restaurant;

}
