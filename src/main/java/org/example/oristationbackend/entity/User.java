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
@NoArgsConstructor
@AllArgsConstructor
public class User {
//    @OneToOne
//    @MapsId
//    @JoinColumn(name = "user_id")
//    private Login login;
    @Id
    private int user_id;

    @Column(length=20)
    private String user_name;

    @Column(length=20)
    private String user_nickname;

    @Column(length=11)
    private String user_phone;
    private boolean is_blocked;
    private Date join_date;
    private Date quit_date;

}
