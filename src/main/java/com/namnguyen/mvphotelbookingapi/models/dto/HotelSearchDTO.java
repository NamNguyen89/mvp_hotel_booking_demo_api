package com.namnguyen.mvphotelbookingapi.models.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class HotelSearchDTO {

    @NotBlank(message = "City cannot be empty")
    private String city;

    @NotNull(message = "Check-in date cannot be empty")
    private LocalDate checkinDate;

    @NotNull(message = "Check-out date cannot be empty")
    private LocalDate checkoutDate;
}
