package com.auction.adauctionbackend.chatmessage.repository;

import com.auction.adauctionbackend.chatmessage.domain.ChatMessage;
import com.auction.adauctionbackend.chatroom.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomOrderBySentAtAsc(ChatRoom chatRoom);
}