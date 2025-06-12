package com.auction.adauctionbackend.user.service;

import com.auction.adauctionbackend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 여기에 사용자 관련 비즈니스 로직을 구현합니다.
}