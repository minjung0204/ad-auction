package com.auction.adauctionbackend.chatmessage.service;

import com.auction.adauctionbackend.chatmessage.domain.ChatMessage;
import com.auction.adauctionbackend.chatmessage.dto.ChatMessageRequest;
import com.auction.adauctionbackend.chatmessage.dto.ChatMessageResponse;
import com.auction.adauctionbackend.chatmessage.repository.ChatMessageRepository;
import com.auction.adauctionbackend.chatroom.domain.ChatRoom;
import com.auction.adauctionbackend.chatroom.repository.ChatRoomRepository;
import com.auction.adauctionbackend.user.domain.User;
import com.auction.adauctionbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatMessageResponse sendMessage(Long chatRoomId, Long senderId, ChatMessageRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found with id: " + chatRoomId));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found with id: " + senderId));

        // TODO: Validate if sender is part of this chat room

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .messageContent(request.getMessageContent())
                .fileUrl(request.getFileUrl())
                .build();

        return ChatMessageResponse.from(chatMessageRepository.save(chatMessage));
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getMessagesByChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found with id: " + chatRoomId));

        List<ChatMessage> messages = chatMessageRepository.findByChatRoomOrderBySentAtAsc(chatRoom);

        return messages.stream()
                .map(ChatMessageResponse::from)
                .collect(Collectors.toList());
    }
}