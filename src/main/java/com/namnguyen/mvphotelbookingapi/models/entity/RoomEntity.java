package com.namnguyen.mvphotelbookingapi.models.entity;

import com.namnguyen.mvphotelbookingapi.models.enums.RoomType;
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
@Table(name = "room")
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private HotelEntity hotel;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    private int roomCount;

    private double pricePerNight;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AvailabilityEntity> availabilities = new ArrayList<>();

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", hotel=" + hotel +
                ", roomType=" + roomType +
                ", roomCount=" + roomCount +
                ", pricePerNight=" + pricePerNight +
                '}';
    }
}
