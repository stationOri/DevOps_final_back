package org.example.oristationbackend.repository;

import org.example.oristationbackend.dto.user.ReviewRestDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewCustomRepository {
    List<ReviewRestDto> findReviewAndLikesByRestaurantIdAndUserId(int restId,int userId);
}
