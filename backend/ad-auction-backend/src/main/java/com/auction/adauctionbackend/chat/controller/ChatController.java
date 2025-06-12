package com.auction.adauctionbackend.chat.controller;

import com.auction.adauctionbackend.chatmessage.dto.ChatMessageRequest;
import com.auction.adauctionbackend.chatmessage.dto.ChatMessageResponse;
import com.auction.adauctionbackend.chatmessage.service.ChatMessageService;
import com.auction.adauctionbackend.chatroom.dto.ChatRoomResponse;
import com.auction.adauctionbackend.chatroom.service.ChatRoomService;
import com.auction.adauctionbackend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    /**
     * 채팅방 생성 (광고주가 대행사를 선택했을 때 또는 직접 요청할 때)
     * 
     * @param bidId        입찰 ID (선택 사항)
     * @param advertiserId 광고주 ID
     * @param agencyId     대행사 ID
     * @return 생성된 채팅방 정보
     */
    @PostMapping("/rooms")
    public ResponseEntity<ChatRoomResponse> createChatRoom(@RequestParam(required = false) Long bidId,
            @RequestParam Long advertiserId,
            @RequestParam Long agencyId) {
        // TODO: bidId가 제공되었을 때 bid와 advertiser, agency의 관계 유효성 검사
        // 현재 로그인된 사용자가 advertiserId 또는 agencyId와 일치하는지 확인하는 로직 추가 필요
        ChatRoomResponse response = chatRoomService.createChatRoom(bidId, advertiserId, agencyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 현재 사용자가 참여하고 있는 모든 채팅방 목록 조회
     * 
     * @param userDetails 현재 인증된 사용자 정보
     * @return 채팅방 목록
     */
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponse>> getMyChatRooms(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ChatRoomResponse> chatRooms = chatRoomService.getChatRoomsForUser(userDetails.getId());
        return ResponseEntity.ok(chatRooms);
    }

    /**
     * 특정 채팅방의 메시지 조회
     * 
     * @param chatRoomId  조회할 채팅방 ID
     * @param userDetails 현재 인증된 사용자 정보
     * @return 메시지 목록
     */
    @GetMapping("/rooms/{chat_room_id}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getChatRoomMessages(@PathVariable("chat_room_id") Long chatRoomId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // TODO: 현재 사용자가 해당 채팅방에 참여하고 있는지 유효성 검사
        List<ChatMessageResponse> messages = chatMessageService.getMessagesByChatRoom(chatRoomId);
        return ResponseEntity.ok(messages);
    }

    /**
     * 특정 채팅방에 메시지 전송
     * 
     * @param chatRoomId  메시지를 전송할 채팅방 ID
     * @param request     메시지 내용 및 파일 URL
     * @param userDetails 현재 인증된 사용자 정보
     * @return 전송된 메시지 정보
     */
    @PostMapping("/rooms/{chat_room_id}/messages")
    public ResponseEntity<ChatMessageResponse> sendMessage(@PathVariable("chat_room_id") Long chatRoomId,
            @RequestBody ChatMessageRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // TODO: 현재 사용자가 해당 채팅방에 참여하고 있는지 유효성 검사
        ChatMessageResponse response = chatMessageService.sendMessage(chatRoomId, userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}