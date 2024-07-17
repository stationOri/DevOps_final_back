package org.example.oristationbackend.service;

import jakarta.persistence.EntityManager;
import org.example.oristationbackend.dto.user.ReviewReqDto;
import org.example.oristationbackend.dto.user.ReviewResDto;
import org.example.oristationbackend.dto.user.ReviewRestDto;
import org.example.oristationbackend.dto.user.WaitingReqDto;
import org.example.oristationbackend.entity.Login;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.RestaurantInfo;
import org.example.oristationbackend.entity.User;
import org.example.oristationbackend.entity.type.*;
import org.example.oristationbackend.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@DisplayName("웨이팅 서비스 테스트")
class WaitingServiceTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private UserRepository userRepository;
   @Autowired
   private RestaurantInfoRepository restaurantInfoRepository;
    @Autowired
    private WaitingService waitingService;

    @Test
    @Transactional
    void 웨이팅() {

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

        RestaurantInfo restaurantInfo2 = new RestaurantInfo(restaurant2.getRestId(),10, MoneyMethod.A,"가게 주소","가게 소개","01012341234", PeriodType.WEEK, MinuteType.HALFHOUR,4.3,
                10,4,"restPost",ReservationType.C,RestWatingStatus.A,restaurant2,null,null,null);
        RestaurantInfo restaurantInfo = new RestaurantInfo(restaurant.getRestId(),10, MoneyMethod.A,"가게 주소","가게 소개","01012341234", PeriodType.WEEK, MinuteType.HALFHOUR,4.3,
                10,4,"restPost",ReservationType.C,RestWatingStatus.C,restaurant,null,null,null);
        restaurant.setRestaurantInfo(restaurantInfo);
        restaurant2.setRestaurantInfo(restaurantInfo2);
        restaurantRepository.save(restaurant2);
        restaurantRepository.save(restaurant);

        WaitingReqDto waitingReqDto = new WaitingReqDto(user.getUserId(),restaurant2.getRestId(),3,"12341234");
        assertEquals(0,waitingService.addWaiting(waitingReqDto));
        WaitingReqDto waitingReqDto2 = new WaitingReqDto(user.getUserId(),restaurant.getRestId(),3,"12341234");
        assertTrue(waitingService.addWaiting(waitingReqDto2) > 0);
        WaitingReqDto waitingReqDto4 = new WaitingReqDto(user.getUserId(),restaurant2.getRestId(),3,"12341234");
        assertEquals(-1,waitingService.addWaiting(waitingReqDto4));
        assertEquals(1,waitingService.getRestWaiting(restaurant.getRestId()).size());

    }




}