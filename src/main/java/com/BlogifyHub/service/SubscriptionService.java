package com.BlogifyHub.service;

import com.BlogifyHub.model.DTO.ProfileResponseDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface SubscriptionService {
    void subscribe(Long userId, Authentication authentication);

    void unsubscribe(Long userId, Authentication authentication);

    List<ProfileResponseDTO> getSubscribers(Long userId);
}
