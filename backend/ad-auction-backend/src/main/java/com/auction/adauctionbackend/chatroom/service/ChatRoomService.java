package com.auction.adauctionbackend.chatroom.service;

import com.auction.adauctionbackend.bid.domain.Bid;
import com.auction.adauctionbackend.bid.repository.BidRepository;
import com.auction.adauctionbackend.chatroom.domain.ChatRoom;
import com.auction.adauctionbackend.chatroom.dto.ChatRoomResponse;
import com.auction.adauctionbackend.chatroom.repository.ChatRoomRepository;
import com.auction.adauctionbackend.user.domain.User;
import com.auction.adauctionbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final BidRepository bidRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatRoomResponse createChatRoom(Long bidId, Long advertiserId, Long agencyId) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new IllegalArgumentException("Bid not found with id: " + bidId));
        User advertiser = userRepository.findById(advertiserId)
                .orElseThrow(() -> new IllegalArgumentException("Advertiser not found with id: " + advertiserId));
        User agency = userRepository.findById(agencyId)
                .orElseThrow(() -> new IllegalArgumentException("Agency not found with id: " + agencyId));

        // TODO: Validate if a chat room already exists for this bid, advertiser, and
        // agency combination
        Optional<ChatRoom> existingChatRoom = chatRoomRepository.findByBidAndAdvertiserAndAgency(bid, advertiser,
                agency);
        if (existingChatRoom.isPresent()) {
            return ChatRoomResponse.from(existingChatRoom.get());
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .bid(bid)
                .advertiser(advertiser)
                .agency(agency)
                .build();

        return ChatRoomResponse.from(chatRoomRepository.save(chatRoom));
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getChatRoomsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        List<ChatRoom> chatRooms = chatRoomRepository.findByAdvertiserOrAgency(user, user);

        return chatRooms.stream()
                .map(ChatRoomResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ChatRoomResponse getChatRoomById(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found with id: " + chatRoomId));
        return ChatRoomResponse.from(chatRoom);
    }
}