package com.namnguyen.mvphotelbookingapi.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "hotel_manager")
public class HotelManagerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "hotelManager", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HotelEntity> hotelList = new ArrayList<>();

    @Override
    public String toString() {
        return "HotelManager{" +
                "id=" + id +
                ", user=" + user +
                ", hotelList=" + hotelList +
                '}';
    }
}
