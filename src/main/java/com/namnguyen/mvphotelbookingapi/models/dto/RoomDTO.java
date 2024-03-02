package com.namnguyen.mvphotelbookingapi.models.dto;

import com.namnguyen.mvphotelbookingapi.models.enums.RoomType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {

    private Long id;

    private Long hotelId;

    private RoomType roomType;

    @NotNull(message = "Room count cannot be empty")
    private Integer roomCount;

    @NotNull(message = "Price cannot be empty")
    private Double pricePerNight;

}