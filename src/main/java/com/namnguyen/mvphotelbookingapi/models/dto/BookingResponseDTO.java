package com.namnguyen.mvphotelbookingapi.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDTO {
    private Long id;
    private String bookingCode;
    private String guestName;
    private String guestPhoneNumber;
    private String guestEmail;
    private Date checkInDate;
    private Date checkOutDate;
    private int totalGuestsBooking;
}
