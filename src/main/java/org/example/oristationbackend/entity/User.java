package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Login login;
    @Id
    private int userId;

    @Column(length=20)
    private String userName;

    @Column(length=20)
    private String userNickname;

    @Column(length=11)
    private String userPhone;
    private boolean isBlocked;
    private Date joinDate;
    private Date quitDate;

    public User quitUser(){
        this.quitDate=new java.sql.Date(System.currentTimeMillis());
        return this;
    }

}
