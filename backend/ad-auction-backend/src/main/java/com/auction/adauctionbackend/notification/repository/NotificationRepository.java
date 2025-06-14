package com.auction.adauctionbackend.notification.repository;

import com.auction.adauctionbackend.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}