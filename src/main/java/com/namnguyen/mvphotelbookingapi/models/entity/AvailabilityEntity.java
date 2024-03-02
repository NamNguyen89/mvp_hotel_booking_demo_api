package com.namnguyen.mvphotelbookingapi.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "availability")
public class AvailabilityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private HotelEntity hotel;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(nullable = false)
    private RoomEntity room;

    @Column(nullable = false)
    private int availableRooms;

    @Override
    public String toString() {
        return "Availability{" +
                "id=" + id +
                ", hotel=" + hotel +
                ", date=" + date +
                ", room=" + room +
                ", availableRooms=" + availableRooms +
                '}';
    }
}
