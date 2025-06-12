package com.auction.adauctionbackend.payment;

import com.auction.adauctionbackend.bid.Bid;
import com.auction.adauctionbackend.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "bid_id", nullable = false) // FK (Bid.bid_id)
    private Bid bid;

    @ManyToOne
    @JoinColumn(name = "advertiser_id", nullable = false) // FK (User.user_id)
    private User advertiser;

    @ManyToOne
    @JoinColumn(name = "agency_id", nullable = false) // FK (User.user_id)
    private User agency;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "platform_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal platformFee;

    @Column(name = "payment_method", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; // BANK_TRANSFER

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // PENDING, COMPLETED, REFUNDED

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Enums
    public enum PaymentMethod {
        BANK_TRANSFER
    }

    public enum PaymentStatus {
        PENDING, COMPLETED, REFUNDED
    }
}