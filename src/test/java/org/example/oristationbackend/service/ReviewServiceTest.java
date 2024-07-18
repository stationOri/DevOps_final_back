package org.example.oristationbackend.service;

import jakarta.persistence.EntityManager;
import org.example.oristationbackend.dto.user.ReviewReqDto;
import org.example.oristationbackend.dto.user.ReviewResDto;
import org.example.oristationbackend.dto.user.ReviewRestDto;
import org.example.oristationbackend.entity.Login;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.User;
import org.example.oristationbackend.entity.type.ChatType;
import org.example.oristationbackend.entity.type.RestaurantStatus;
import org.example.oristationbackend.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("리뷰 관련 테스트")
class ReviewServiceTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewLikesRepository reviewLikesRepository;

    @Test
    @Transactional
    void 유저추가() {

        Login login = new Login(0, "email@email", "password", ChatType.USER, null, null, null);
        login = loginRepository.save(login); // Save the login entity

        // Create a User entity and associate it with the saved login entity
        User user = new User(login, 0, "이름", "닉네임", "01023451234", false, new Date(System.currentTimeMillis()), null);
        user = userRepository.save(user); // Save the user entity

        // Associate the login entity with the user entity and save it again
        login.setUser(user);
        loginRepository.save(login);

        Login login2 = new Login(0, "email@email", "password", ChatType.USER, null, null, null);
        entityManager.persist(login2); // 영속 엔티티로 관리됨
        Restaurant restaurant= new Restaurant(login2,0,"식당이름","01012341234","식당이름","사진경로","","23",false, new Date(System.currentTimeMillis()),null, RestaurantStatus.B,true,"계좌",null);        user=userRepository.save(user);
        restaurant=restaurantRepository.save(restaurant);
        login.setRestaurant(restaurant);
        loginRepository.save(login2);
    }


    @Test
    @Transactional
    void 리뷰테스트() {
        Login login = new Login(0, "email@email", "password", ChatType.USER, null, null, null);
        entityManager.persist(login); // 영속 엔티티로 관리됨
        User user = new User(login, 0, "이름", "닉네임", "01023451234", false, new Date(System.currentTimeMillis()), null);
        user=userRepository.save(user);
        login.setUser(user);
        loginRepository.save(login);

        Login login2 = new Login(0, "email@email", "password", ChatType.USER, null, null, null);
        entityManager.persist(login2); // 영속 엔티티로 관리됨
        Restaurant restaurant= new Restaurant(login2,0,"식당이름","01012341234","식당이름","사진경로","","23",false, new Date(System.currentTimeMillis()),null, RestaurantStatus.B,true,"계좌",null);        user=userRepository.save(user);
        restaurant=restaurantRepository.save(restaurant);
        login.setRestaurant(restaurant);
        loginRepository.save(login2);

        Login login3 = new Login(0, "email@email", "password", ChatType.USER, null, null, null);
        entityManager.persist(login3); // 영속 엔티티로 관리됨
        User user2 = new User(login3, 0, "이름2", "닉네임2", "01023451234", false, new Date(System.currentTimeMillis()), null);
        user2=userRepository.save(user2);
        login3.setUser(user2);
        loginRepository.save(login3);

        Login login4 = new Login(0, "email@email", "password", ChatType.USER, null, null, null);
        entityManager.persist(login4); // 영속 엔티티로 관리됨
        Restaurant restaurant2= new Restaurant(login4,0,"식당이름2","01012341234","식당이름2","사진경로","","23",false, new Date(System.currentTimeMillis()),null, RestaurantStatus.B,true,"계좌",null);        user=userRepository.save(user);
        restaurant2=restaurantRepository.save(restaurant2);
        login4.setRestaurant(restaurant2);
        loginRepository.save(login4);

        ReviewReqDto reviewReqDto=new ReviewReqDto(user.getUserId(),restaurant.getRestId(),"1번 리뷰내용입니다",3.4,null,null,null);
        ReviewReqDto reviewReqDto2=new ReviewReqDto(user2.getUserId(),restaurant.getRestId(),"2번 리뷰내용입니다",3.4,null,null,null);
        ReviewReqDto reviewReqDto3=new ReviewReqDto(user2.getUserId(),restaurant2.getRestId(),"3번 리뷰내용입니다",3.4,null,null,null);

        reviewService.addReview(reviewReqDto);
        reviewService.addReview(reviewReqDto2);
        int id=reviewService.addReview(reviewReqDto3);

        List<ReviewRestDto> list=reviewService.getReviewsByrestId(restaurant.getRestId(),user2.getUserId());
        List<ReviewResDto> list2=reviewService.getReviewsByuserId(user2.getUserId());

        // Assertion 추가 예시 (필요시)
        assertEquals(2, list.size(), "Expected 2 reviews for Restaurant1 by User2");
        assertEquals(2, list2.size(), "Expected 2 reviews for User2");

        reviewService.likeReview(id,user.getUserId());
        assertEquals(1,reviewRepository.findById(id).get().getLikeNum(),"Expected 1 like for review");
        reviewService.deleteReview(id);
        assertEquals(0,reviewLikesRepository.count());
    }
}