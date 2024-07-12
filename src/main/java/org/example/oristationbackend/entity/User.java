package org.example.oristationbackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Getter
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

}