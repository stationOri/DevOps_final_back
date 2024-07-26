package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Entity
@Data
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private Login login;
    @Id
    private int userId; // 사용자 id

    @Column(length=20)
    private String userName; // 사용자 이름

    @Column(length=20)
    private String userNickname; // 사용자 닉네임

    @Column(length=11)
    private String userPhone; // 사용자 전화번호

    private boolean isBlocked; // 사용자 정지 여부

    private Date joinDate; // 가입일

    private Date quitDate; // 탈퇴일

    public User quitUser(){
        this.isBlocked=true;
        this.quitDate=new java.sql.Date(System.currentTimeMillis());
        return this;
    }

}
