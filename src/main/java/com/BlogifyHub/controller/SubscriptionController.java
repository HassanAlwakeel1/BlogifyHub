package com.BlogifyHub.controller;

import com.BlogifyHub.model.DTO.ProfileResponseDTO;
import com.BlogifyHub.service.SubscriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@Tag(name = "Subscription")
public class SubscriptionController {
    private SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/{userId}")
    public void subscribe(@PathVariable Long userId, Authentication authentication) {
        subscriptionService.subscribe(userId, authentication);
    }

    @DeleteMapping("/{userId}")
    public void unsubscribe(@PathVariable Long userId, Authentication authentication) {
        subscriptionService.unsubscribe(userId, authentication);
    }

    @GetMapping("/{userId}/subscribers")
    public List<ProfileResponseDTO> getSubscribers(@PathVariable Long userId) {
        return subscriptionService.getSubscribers(userId);
    }
}
