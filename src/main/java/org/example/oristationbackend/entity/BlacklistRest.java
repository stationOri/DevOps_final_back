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
  private int blacklistRestId;

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
  @JoinColumn(name = "rest_id")
  private Restaurant restaurant;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "admin_id")
  private Admin admin;
  public BlacklistRest addreport(){
    this.reportNum=this.reportNum+1;
    return this;
  }
  public BlacklistRest changeStatus(BlackStatus status){
    this.blackStatus=status;
    return this;
  }

}
