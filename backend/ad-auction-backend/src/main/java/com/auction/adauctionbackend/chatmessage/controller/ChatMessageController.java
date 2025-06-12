package com.auction.adauctionbackend.chatmessage.controller;

import com.auction.adauctionbackend.chatmessage.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatmessages")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @Autowired
    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    // 여기에 채팅 메시지 관련 API 엔드포인트를 구현합니다.
}