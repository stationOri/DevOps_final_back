package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.oristationbackend.entity.type.BlackStatus;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "blacklist_rest")
@AllArgsConstructor
@NoArgsConstructor
public class BlacklistRest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int blacklistRestId; // 블랙리스트 id

  @Column(name = "processing_date", nullable = false)
  private Timestamp processingDate; // 처리 날짜

  @Enumerated(EnumType.STRING)
  @Column(name = "black_status", nullable = false, length = 1)
  private BlackStatus blackStatus; // 블랙리스트 상태

  @Column(name = "report_num", nullable = false)
  private int reportNum; // 신고 횟수

  @Column(name = "ban_start_date", nullable = false)
  private Date banStartDate; // 정지 시작일

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "rest_id")
  private Restaurant restaurant; // 식당 id

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "admin_id")
  private Admin admin; // 관리자 id

  public BlacklistRest addreport(){
    this.reportNum=this.reportNum+1;
    return this;
  }

  public BlacklistRest changeStatus(BlackStatus status){
    this.blackStatus=status;
    return this;
  }

}
