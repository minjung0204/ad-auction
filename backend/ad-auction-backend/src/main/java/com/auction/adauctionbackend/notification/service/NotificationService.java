package com.auction.adauctionbackend.notification.service;

import com.auction.adauctionbackend.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // 여기에 알림 관련 비즈니스 로직을 구현합니다.
}