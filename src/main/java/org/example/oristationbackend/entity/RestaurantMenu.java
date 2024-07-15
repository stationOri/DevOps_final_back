package org.example.oristationbackend.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RestaurantMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int menuId; // 메뉴 id

    @Column(length=20,nullable = false)
    private String menuName; // 메뉴 이름

    @Column(nullable = false)
    private int menuPrice; // 메뉴 가격

    @Column(nullable = false)
    private String menuPhoto; // 메뉴 사진

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rest_id")
    private Restaurant restaurant; // 식당 id
}
