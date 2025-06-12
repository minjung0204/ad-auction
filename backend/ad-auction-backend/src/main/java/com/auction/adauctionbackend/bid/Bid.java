package com.auction.adauctionbackend.bid;

import com.auction.adauctionbackend.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Bid")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_id")
    private Long bidId;

    @ManyToOne
    @JoinColumn(name = "advertiser_id", nullable = false) // FK (User.user_id)
    private User advertiser;

    @Column(name = "place_link", nullable = false, length = 500)
    private String placeLink;

    @Column(name = "desired_rank", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private DesiredRank desiredRank; // 1-5위, 1-10위 등

    @Column(name = "keyword", nullable = false, length = 255)
    private String keyword;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private BidStatus status; // PENDING, ACTIVE, CLOSED, SELECTED, CANCELED

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @ManyToOne
    @JoinColumn(name = "selected_agency_id") // FK (User.user_id), NULL 허용
    private User selectedAgency;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Enums
    public enum DesiredRank {
        RANK_1_5, RANK_1_10, ETC // 예시
    }

    public enum BidStatus {
        PENDING, ACTIVE, CLOSED, SELECTED, CANCELED
    }
}