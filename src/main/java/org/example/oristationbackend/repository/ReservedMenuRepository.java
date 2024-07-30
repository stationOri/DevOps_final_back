package org.example.oristationbackend.repository;

import org.example.oristationbackend.dto.restaurant.MenuDto;
import org.example.oristationbackend.entity.ReservedMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservedMenuRepository extends JpaRepository<ReservedMenu, Integer> {

    @Query("SELECT new org.example.oristationbackend.dto.restaurant.MenuDto(rm.restaurantMenu.menuName, rm.amount) " +
            "FROM ReservedMenu rm " +
            "WHERE rm.reservation.resId = :resId")
    List<MenuDto> findByReservation_ResId(@Param("resId")int resId);

    boolean existsByRestaurantMenu_MenuId(int menuId);
}
