package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.oristationbackend.dto.user.ReviewReqDto;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Table(name = "review")
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    private double reviewGrade;

    @Column(length=500)
    private String reviewData;

    private String reviewImg;
    private String reviewImg2;
    private String reviewImg3;
    private boolean blind;
    private Timestamp reviewDate;
    private int likeNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rest_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewLikes> reviewLikes;

    public Review blindReview(){
        this.blind=true;
        return this;
    }
    public Review like(ReviewLikes reviewlikes){
        this.likeNum++;
        this.reviewLikes.add(reviewlikes);
        return this;
    }

}
