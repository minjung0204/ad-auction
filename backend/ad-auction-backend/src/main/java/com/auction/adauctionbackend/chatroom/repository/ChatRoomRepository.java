package com.auction.adauctionbackend.chatroom.repository;

import com.auction.adauctionbackend.bid.domain.Bid;
import com.auction.adauctionbackend.chatroom.domain.ChatRoom;
import com.auction.adauctionbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByBidAndAdvertiserAndAgency(Bid bid, User advertiser, User agency);

    List<ChatRoom> findByAdvertiserOrAgency(User advertiser, User agency);
}