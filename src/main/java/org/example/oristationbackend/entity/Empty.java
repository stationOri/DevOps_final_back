package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name="emptytable")
public class Empty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int empty_id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rest_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false)
    private Date date;
    @Column(nullable = false)
    private Time time;
    @Column(nullable = false)
    private int people;
    @Column(nullable = false)
    private boolean status;
}
