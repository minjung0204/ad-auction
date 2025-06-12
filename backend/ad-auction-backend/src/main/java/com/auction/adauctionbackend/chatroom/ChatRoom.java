package com.auction.adauctionbackend.chatroom;

import com.auction.adauctionbackend.bid.Bid;
import com.auction.adauctionbackend.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ChatRoom")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long chatRoomId;

    @ManyToOne
    @JoinColumn(name = "bid_id", nullable = false) // FK (Bid.bid_id)
    private Bid bid;

    @ManyToOne
    @JoinColumn(name = "advertiser_id", nullable = false) // FK (User.user_id)
    private User advertiser;

    @ManyToOne
    @JoinColumn(name = "agency_id", nullable = false) // FK (User.user_id)
    private User agency;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}