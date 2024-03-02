package com.namnguyen.mvphotelbookingapi.models.dto;


import com.namnguyen.mvphotelbookingapi.models.enums.RoomType;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomResponseDTO {
    private Long id;
    private String roomNumber;
    private String roomType;
    private boolean isBooked = false;
    private BigDecimal price;
    private List<BookingResponseDTO> bookingList = new ArrayList<BookingResponseDTO>();

    //add new room
    public RoomResponseDTO(Long id, RoomType roomType, BigDecimal price) {
        this.id = id;
        this.roomType = roomType.name();
        this.price = price;
    }
}
