package com.auction.adauctionbackend.chatroom.service;

import com.auction.adauctionbackend.chatroom.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Autowired
    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    // 여기에 채팅방 관련 비즈니스 로직을 구현합니다.
}