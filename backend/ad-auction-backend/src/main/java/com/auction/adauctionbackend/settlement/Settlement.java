package com.auction.adauctionbackend.settlement;

import com.auction.adauctionbackend.payment.Payment;
import com.auction.adauctionbackend.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Settlement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settlement_id")
    private Long settlementId;

    @OneToOne
    @JoinColumn(name = "payment_id", nullable = false) // FK (Payment.payment_id)
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "agency_id", nullable = false) // FK (User.user_id)
    private User agency;

    @Column(name = "settlement_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal settlementAmount;

    @Column(name = "proof_screenshot_url", length = 500)
    private String proofScreenshotUrl;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private SettlementStatus status; // PENDING_PROOF, PENDING_APPROVAL, COMPLETED, REJECTED

    @Column(name = "settled_at")
    private LocalDateTime settledAt;

    @Column(name = "admin_checked_at")
    private LocalDateTime adminCheckedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Enums
    public enum SettlementStatus {
        PENDING_PROOF, PENDING_APPROVAL, COMPLETED, REJECTED
    }
}