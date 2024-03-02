package com.namnguyen.mvphotelbookingapi.models.entity;

import com.namnguyen.mvphotelbookingapi.models.enums.Currency;
import com.namnguyen.mvphotelbookingapi.models.enums.PaymentMethod;
import com.namnguyen.mvphotelbookingapi.models.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String transactionId;

    @CreationTimestamp
    private LocalDateTime paymentDate;

    @OneToOne
    @JoinColumn(nullable = false)
    private BookingEntity booking;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @PrePersist
    protected void onCreate() {
        this.transactionId = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", transactionId=" + transactionId +
                ", paymentDate=" + paymentDate +
                ", booking=" + booking +
                ", totalPrice=" + totalPrice +
                ", paymentStatus=" + paymentStatus +
                ", paymentMethod=" + paymentMethod +
                ", currency=" + currency +
                '}';
    }
}
