package com.auction.adauctionbackend.review;

import com.auction.adauctionbackend.bid.Bid;
import com.auction.adauctionbackend.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "advertiser_id", nullable = false) // FK (User.user_id)
    private User advertiser;

    @ManyToOne
    @JoinColumn(name = "agency_id", nullable = false) // FK (User.user_id)
    private User agency;

    @ManyToOne
    @JoinColumn(name = "bid_id") // FK (Bid.bid_id), NULL 허용
    private Bid bid;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "status", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ReviewStatus status; // ACTIVE, HIDDEN, DELETED

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Enums
    public enum ReviewStatus {
        ACTIVE, HIDDEN, DELETED
    }
}