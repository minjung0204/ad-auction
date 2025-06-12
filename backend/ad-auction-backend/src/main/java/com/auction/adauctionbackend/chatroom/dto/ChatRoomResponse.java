package com.auction.adauctionbackend.chatroom.dto;

import com.auction.adauctionbackend.bid.dto.BidResponse;
import com.auction.adauctionbackend.chatroom.domain.ChatRoom;
import com.auction.adauctionbackend.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomResponse {
    private Long id;
    private BidResponse bid;
    private UserResponse advertiser;
    private UserResponse agency;
    private LocalDateTime createdAt;

    public static ChatRoomResponse from(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .id(chatRoom.getId())
                .bid(BidResponse.from(chatRoom.getBid()))
                .advertiser(UserResponse.from(chatRoom.getAdvertiser()))
                .agency(UserResponse.from(chatRoom.getAgency()))
                .createdAt(chatRoom.getCreatedAt())
                .build();
    }
}