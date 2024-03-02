package com.namnguyen.mvphotelbookingapi.services;

import com.namnguyen.mvphotelbookingapi.models.dto.BookingInitiationDTO;
import com.namnguyen.mvphotelbookingapi.models.entity.BookingEntity;
import com.namnguyen.mvphotelbookingapi.models.entity.PaymentEntity;

public interface PaymentService {

    PaymentEntity savePayment(BookingInitiationDTO bookingInitiationDTO, BookingEntity booking);
}
