package com.namnguyen.mvphotelbookingapi.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "hotel")
public class HotelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private AddressEntity address;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RoomEntity> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "hotel")
    private List<BookingEntity> bookings = new ArrayList<>();

    @ManyToOne
    @JoinColumn(nullable = false)
    private HotelManagerEntity hotelManager;

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", rooms=" + rooms +
                ", hotelManager=" + hotelManager +
                '}';
    }
}
