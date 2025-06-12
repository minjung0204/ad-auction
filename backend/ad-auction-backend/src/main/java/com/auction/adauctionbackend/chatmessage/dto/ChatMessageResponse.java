package com.auction.adauctionbackend.chatmessage.dto;

import com.auction.adauctionbackend.chatmessage.domain.ChatMessage;
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
public class ChatMessageResponse {
    private Long id;
    private Long chatRoomId;
    private UserResponse sender;
    private String messageContent;
    private String fileUrl;
    private LocalDateTime sentAt;

    public static ChatMessageResponse from(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .id(chatMessage.getId())
                .chatRoomId(chatMessage.getChatRoom().getId())
                .sender(UserResponse.from(chatMessage.getSender()))
                .messageContent(chatMessage.getMessageContent())
                .fileUrl(chatMessage.getFileUrl())
                .sentAt(chatMessage.getSentAt())
                .build();
    }
}