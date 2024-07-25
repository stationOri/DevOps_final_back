package org.example.oristationbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.oristationbackend.dto.admin.restAcceptReadyDto;
import org.example.oristationbackend.dto.admin.restAfterAcceptDto;
import org.example.oristationbackend.dto.restaurant.MenuAddReqDto;
import org.example.oristationbackend.dto.restaurant.MenuListResDto;
import org.example.oristationbackend.dto.restaurant.MenuModReqDto;
import org.example.oristationbackend.dto.user.*;
import org.example.oristationbackend.dto.restaurant.*;
import org.example.oristationbackend.entity.type.RestaurantStatus;
import org.example.oristationbackend.repository.RestaurantMenuRepository;
import org.example.oristationbackend.repository.RestaurantPeakRepository;
import org.example.oristationbackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final RestaurantMenuService restaurantMenuService;
    private final RestTempHolidayService restTempHolidayService;
    private final RestaurantPeakRepository restaurantPeakRepository;
    private final RestaurantPeakService restaurantPeakService;
    private final RestaurantMenuRepository restaurantMenuRepository;
    private final RestaurantInfoService restaurantInfoService;
    private ObjectMapper objectMapper;

    @Autowired
    public RestaurantController(RestaurantService restaurantService, RestaurantMenuService restaurantMenuService, RestTempHolidayService restTempHolidayService, RestaurantPeakRepository restaurantPeakRepository, RestaurantPeakService restaurantPeakService, RestaurantMenuRepository restaurantMenuRepository, ObjectMapper objectMapper, RestaurantInfoService restaurantInfoService) {
        this.restaurantService = restaurantService;
        this.restaurantMenuService = restaurantMenuService;
        this.restTempHolidayService = restTempHolidayService;
        this.restaurantPeakRepository = restaurantPeakRepository;
        this.restaurantPeakService = restaurantPeakService;
        this.restaurantMenuRepository = restaurantMenuRepository;
        this.objectMapper = new ObjectMapper();
        this.restaurantInfoService = restaurantInfoService;
    }

    // 식당 전체 조회
    @GetMapping()
    public ResponseEntity<List<SearchResDto>> getAllRestaurants() {
        List<SearchResDto> restaurants = restaurantService.findAllRestaurants();
        if (restaurants.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(restaurants);
        }
    }

    // 식당 페이지별 조회
    @GetMapping("/page/{page}")
    public ResponseEntity<List<SearchResDto>> getRestaurantsByPage(@PathVariable("page") int page) {
        List<SearchResDto> restaurants = restaurantService.getRestaurantsByPage(page);

        if (restaurants.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(restaurants);
        }
    }

    // 식당 id로 식당 정보 조회
    @GetMapping("/{restId}")
    public ResponseEntity<SearchResDto> getRestaurantById(@PathVariable(name = "restId") int restId) {
        SearchResDto restaurant = restaurantService.findRestaurantById(restId);
        if (restaurant == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(restaurant);
        }
    }

    //식당이 환불받을 예약금 조회
    @GetMapping("/deposit/{restId}")
    public int getRestaurantDeposit(@PathVariable(name = "restId") int restId) {
        return restaurantService.getRestaurantDeposit(restId);
    }

    // 식당 이름으로 식당 정보 조회
    @GetMapping("/name/{restName}")
    public ResponseEntity<List<SearchResDto>> getRestaurantsByName(@PathVariable(name = "restName") String restName) {
        List<SearchResDto> restaurants = restaurantService.findRestaurantsByName(restName);
        if (restaurants.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(restaurants);
        }
    }

    // 식당 id로 식당 메뉴 전체 조회
    @GetMapping("/menu/{restId}")
    public ResponseEntity<List<MenuListResDto>> getAllMenusByRestId(@PathVariable(name = "restId") int restId) {
        List<MenuListResDto> menus = restaurantMenuService.getAllMenusByRestaurantId(restId);
        if (menus.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(menus);
        }
    }

    // 메뉴 추가
    @PostMapping("/menu")
    public ResponseEntity<Integer> addRestaurantMenu(
        @RequestParam("menuData") String menuData,
        @RequestParam("file") MultipartFile file) {
      try {
        MenuAddReqDto menuAddReqDto = new ObjectMapper().readValue(menuData, MenuAddReqDto.class);
        int menuId = restaurantMenuService.addRestaurantMenu(menuAddReqDto, file);
        return ResponseEntity.ok(menuId);
      } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError().body(null);
      }
    }

    // 메뉴 수정
    @PutMapping("/menu/{menuId}")
    public ResponseEntity<Integer> updateRestaurantMenu(
        @PathVariable(name = "menuId") int menuId,
        @RequestParam(value = "menuData", required = false) String menuDataJson,
        @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

      MenuModReqDto menuModReqDto = menuDataJson != null ? new ObjectMapper().readValue(menuDataJson, MenuModReqDto.class) : new MenuModReqDto();

      int updatedMenuId = restaurantMenuService.updateRestaurantMenu(menuId, menuModReqDto, file);
      return ResponseEntity.ok(updatedMenuId);
    }

    // 메뉴 id로 메뉴 삭제
    @DeleteMapping("/menu/{menuId}")
    public ResponseEntity<Void> deleteRestaurantMenu(@PathVariable(name = "menuId") int menuId) {
        restaurantMenuService.deleteRestaurantMenu(menuId);
        return ResponseEntity.ok().build();
    }

    //식당 승인 전
    @GetMapping("/beforeAccept")
    public ResponseEntity<List<restAcceptReadyDto>> getRestaurantsBeforeAccept() {
        List<restAcceptReadyDto> resultRestaurants = restaurantService.findRestraurantByStatusBefore(RestaurantStatus.A);
        if (resultRestaurants == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(resultRestaurants);
        }
    }

    //식당 승인 후
    @GetMapping("/afterAccept")
    public ResponseEntity<List<restAfterAcceptDto>> getRestaurantsAfterAccept() {
        List<restAfterAcceptDto> resultRestaurants = restaurantService.findRestraurantByStatusAfter(RestaurantStatus.B);
        if (resultRestaurants == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(resultRestaurants);
        }
    }

    //식당 승인 및 거절
    @PutMapping("/status/{rest_id}")
    public int updateRestStatus(@PathVariable(name = "rest_id") int restId, @RequestBody Map<String, String> body) {
        String statusStr = body.get("status");
        RestaurantStatus status = RestaurantStatus.fromValue(statusStr);
        return restaurantService.updateRestaurantStatus(status, restId);
    }

    // 최근 2년간 가장 많이 예약된 식당 조회
    @GetMapping("/most/{userId}")
    public List<MostRestDto> getMostReservedRestaurantsByUser(@PathVariable(name = "userId") int userId) {
        return restaurantService.getMostReservedRestaurantsByUser(userId);
    }

  // 추천 식당 조회 (평점 4.0 이상, 최근 3개월간 예약이 가장 많은 식당 7개)
  @GetMapping("/recommend")
  public ResponseEntity<List<RecommendRestDto>> getRecommendedRestaurants() {
    List<RecommendRestDto> recommendedRestaurants = restaurantService.getRecommendedRestaurants();
    return ResponseEntity.ok(recommendedRestaurants);
  }

  // 최근 2주 동안 예약이 가장 많은 식당 조회
  @GetMapping("/hot")
  public ResponseEntity<List<HotRestDto>> getHotRestaurants() {
    List<HotRestDto> hotRestaurants = restaurantService.getHotRestaurants();
    return ResponseEntity.ok(hotRestaurants);
  }

  // 주변 식당 조회(사용자 위치로부터 주변 5km 이내의 식당)
  @GetMapping("/near/lat/{lat}/lng/{lng}")
  public List<NearRestDto> getNearbyRestaurants(@PathVariable(name = "lat") double lat, @PathVariable(name = "lng") double lng) {
    return restaurantService.getNearbyRestaurants(lat, lng);
  }

    //임시휴무 받아오기
    @GetMapping("/rest-temp-holiday/{rest-id}")
    public ResponseEntity<List<TempHolidayResDto>> gettempholidaybyRestId(@PathVariable(name = "rest-id") int restId) {
        List<TempHolidayResDto> resultRestaurants = restTempHolidayService.getTempHolidays(restId);
        if (resultRestaurants == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(resultRestaurants);
        }
    }

    //임시휴무 삭제 /restaurant/rest-temp-holiday/{temp_holiday_id}
    @DeleteMapping("/rest-temp-holiday/{temp_holiday_id}")
    public int deleteTempHoliday(@PathVariable(name = "temp_holiday_id") int tempHolidayId) {
        return restTempHolidayService.deleteTempHoliday(tempHolidayId);
    }

    //임시휴무 등록
    @PostMapping("/rest-temp-holiday")
    public int addTempHoliday(@RequestBody TempHolidayReqDto tempHolidayReqDto) {
        return restTempHolidayService.addTempholiday(tempHolidayReqDto);
    }

    //성수기 등록
    @PostMapping("/peak")
    public int addPeak(@RequestBody PeakReqDto peakReqDto) {
        return restaurantPeakService.addRestaurantPeak(peakReqDto);
    }

    //성수기 받아오기
    @GetMapping("/peak/{rest-id}")
    public ResponseEntity<List<PeakListResDto>> getPeakList(@PathVariable(name = "rest-id") int restId) {
        List<PeakListResDto> resultRestaurants = restaurantPeakService.getPeakHolidays(restId);
        if (resultRestaurants == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(resultRestaurants);
        }
    }

    //성수기 삭제
    @DeleteMapping("/peak/{peak_id}")
    public int deletePeak(@PathVariable(name = "peak_id") int peakId) {
        return restaurantPeakService.deleteRestaurantPeak(peakId);
    }



}