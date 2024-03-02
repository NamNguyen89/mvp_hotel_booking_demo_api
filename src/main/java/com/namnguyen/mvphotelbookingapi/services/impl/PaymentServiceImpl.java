package com.namnguyen.mvphotelbookingapi.services.impl;

import com.namnguyen.mvphotelbookingapi.models.dto.BookingInitiationDTO;
import com.namnguyen.mvphotelbookingapi.models.entity.BookingEntity;
import com.namnguyen.mvphotelbookingapi.models.entity.PaymentEntity;
import com.namnguyen.mvphotelbookingapi.models.enums.Currency;
import com.namnguyen.mvphotelbookingapi.models.enums.PaymentMethod;
import com.namnguyen.mvphotelbookingapi.models.enums.PaymentStatus;
import com.namnguyen.mvphotelbookingapi.repository.PaymentRepository;
import com.namnguyen.mvphotelbookingapi.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentEntity savePayment(BookingInitiationDTO bookingInitiationDTO, BookingEntity booking) {
        PaymentEntity payment = PaymentEntity.builder()
                .booking(booking)
                .totalPrice(bookingInitiationDTO.getTotalPrice())
                .paymentStatus(PaymentStatus.COMPLETED) // Assuming the payment is completed
                .paymentMethod(PaymentMethod.CREDIT_CARD) // Default to CREDIT_CARD
                .currency(Currency.USD) // Default to USD
                .build();

        PaymentEntity savedPayment = paymentRepository.save(payment);
        log.info("Payment saved with transaction ID: {}", savedPayment.getTransactionId());

        return savedPayment;
    }
}
