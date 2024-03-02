package com.namnguyen.mvphotelbookingapi.models.entity;

import com.namnguyen.mvphotelbookingapi.models.enums.RoomType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Booked_Room")
public class BookedRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private BookingEntity booking;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @Column(nullable = false)
    private int count;

    @Override
    public String toString() {
        return "BookedRoom{" +
                "id=" + id +
                ", booking=" + booking +
                ", roomType=" + roomType +
                ", count=" + count +
                '}';
    }
}
