package org.example.oristationbackend.service;

import org.example.oristationbackend.dto.restaurant.RestaurantOpenDto;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.RestaurantOpen;
import org.example.oristationbackend.entity.type.OpenDay;
import org.example.oristationbackend.repository.RestaurantOpenRepository;
import org.example.oristationbackend.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RestOpenServiceTest {

  @Mock
  private RestaurantOpenRepository restaurantOpenRepository;

  @Mock
  private RestaurantRepository restaurantRepository;

  @InjectMocks
  private RestaurantOpenService restOpenService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testFindByRestId() {
    // Given
    int restId = 1;
    RestaurantOpen restaurantOpen = new RestaurantOpen();
    restaurantOpen.setRestaurantOpenId(1);
    restaurantOpen.setRestDay(OpenDay.MON);
    restaurantOpen.setRestOpen("12:00");
    restaurantOpen.setRestClose("22:00");

    when(restaurantRepository.findById(restId)).thenReturn(Optional.of(new Restaurant()));
    when(restaurantOpenRepository.findByRestaurant(any())).thenReturn(Arrays.asList(restaurantOpen));

    // When
    List<RestaurantOpenDto> restaurantOpenDtos = restOpenService.findByRestId(restId);

    // Then
    assertThat(restaurantOpenDtos).isNotNull();
    assertThat(restaurantOpenDtos).hasSizeGreaterThan(0);
    assertThat(restaurantOpenDtos.get(0).getRestaurantOpenId()).isEqualTo(1);
  }

  @Test
  void testCreateRestaurantOpen() {
    // Given
    RestaurantOpenDto restaurantOpenDto = new RestaurantOpenDto();
    restaurantOpenDto.setRestId(1);
    restaurantOpenDto.setRestDay(OpenDay.THU);
    restaurantOpenDto.setRestOpen("12:00");
    restaurantOpenDto.setRestClose("22:00");

    RestaurantOpen restaurantOpen = new RestaurantOpen();
    restaurantOpen.setRestaurantOpenId(1);
    restaurantOpen.setRestDay(restaurantOpenDto.getRestDay());
    restaurantOpen.setRestOpen(restaurantOpenDto.getRestOpen());
    restaurantOpen.setRestClose(restaurantOpenDto.getRestClose());

    when(restaurantRepository.findById(restaurantOpenDto.getRestId())).thenReturn(Optional.of(new Restaurant()));
    when(restaurantOpenRepository.save(any())).thenReturn(restaurantOpen);

    // When
    RestaurantOpenDto createdOpenTime = restOpenService.createRestaurantOpen(restaurantOpenDto);

    // Then
    assertThat(createdOpenTime).isNotNull();
    assertThat(createdOpenTime.getRestaurantOpenId()).isEqualTo(1);
    verify(restaurantOpenRepository, times(1)).save(any());
  }

  @Test
  void testUpdateRestaurantOpen() {
    // Given
    RestaurantOpenDto restaurantOpenDto = new RestaurantOpenDto();
    restaurantOpenDto.setRestaurantOpenId(1);
    restaurantOpenDto.setRestDay(OpenDay.MON);
    restaurantOpenDto.setRestOpen("12:00");
    restaurantOpenDto.setRestClose("22:00");

    RestaurantOpen restaurantOpen = new RestaurantOpen();
    restaurantOpen.setRestaurantOpenId(1);
    restaurantOpen.setRestDay(restaurantOpenDto.getRestDay());
    restaurantOpen.setRestOpen(restaurantOpenDto.getRestOpen());
    restaurantOpen.setRestClose(restaurantOpenDto.getRestClose());

    when(restaurantOpenRepository.findById(restaurantOpenDto.getRestaurantOpenId())).thenReturn(Optional.of(restaurantOpen));
    when(restaurantOpenRepository.save(any())).thenReturn(restaurantOpen);

    // When
    RestaurantOpenDto updatedOpenTime = restOpenService.updateRestaurantOpen(restaurantOpenDto);

    // Then
    assertThat(updatedOpenTime).isNotNull();
    assertThat(updatedOpenTime.getRestaurantOpenId()).isEqualTo(1);
    verify(restaurantOpenRepository, times(1)).save(any());
  }
}