package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.oristationbackend.entity.type.BlackStatus;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Getter
@Table(name = "blacklist_user")
@AllArgsConstructor
@NoArgsConstructor
public class BlacklistUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int blacklistUserId; // 블랙리스트 id

  @Column(name = "processing_date", nullable = false)
  private Timestamp processingDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "black_status", nullable = false, length = 1)
  private BlackStatus blackStatus;

  @Column(name = "report_num", nullable = false)
  private int reportNum;

  @Column(name = "ban_start_date", nullable = false)
  private Date banStartDate;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "admin_id")
  private Admin admin;
  
  public BlacklistUser addreport(){
    this.reportNum=this.reportNum+1;
      return this;
  }
  public void changeStatus(BlackStatus status){
    this.blackStatus=status;
  }
}
