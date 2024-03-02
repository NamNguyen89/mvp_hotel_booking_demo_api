package com.namnguyen.mvphotelbookingapi.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CreditCardNumber;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCardDTO {

    @NotBlank(message = "Cardholder name cannot be empty")
    private String cardholderName;

    @CreditCardNumber(message = "Invalid credit card number")
    private String cardNumber;

    private String expirationDate;

    private String cvc;
}
