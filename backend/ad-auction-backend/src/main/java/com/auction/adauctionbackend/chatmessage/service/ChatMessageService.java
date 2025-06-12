package com.auction.adauctionbackend.chatmessage.service;

import com.auction.adauctionbackend.chatmessage.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    // 여기에 채팅 메시지 관련 비즈니스 로직을 구현합니다.
}