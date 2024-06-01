package com.BlogifyHub.service.impl;

import com.BlogifyHub.exception.ResourceNotFoundException;
import com.BlogifyHub.model.DTO.ProfileResponseDTO;
import com.BlogifyHub.model.entity.Subscription;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.model.mapper.UserMapper;
import com.BlogifyHub.repository.SubscriptionRepository;
import com.BlogifyHub.repository.UserRepository;
import com.BlogifyHub.service.SubscriptionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private UserRepository userRepository;

    private SubscriptionRepository subscriptionRepository;

    private UserMapper userMapper;

    public SubscriptionServiceImpl(UserRepository userRepository, SubscriptionRepository subscriptionRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.userMapper = userMapper;
    }

    @Override
    public void subscribe(Long userId, Authentication authentication) {
        String email = authentication.getName();
        User subscriber = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (subscriber.equals(user)) {
            throw new RuntimeException("You can't subscribe to yourself");
        }

        if (isSubscribed(subscriber, user)) {
            throw new RuntimeException("You are already subscribed to this user");
        }
        Subscription subscription = new Subscription();
        subscription.setSubscriber(subscriber);
        subscription.setUser(user);
        subscriptionRepository.save(subscription);

        user.setSubscribersNumber(user.getSubscribersNumber() + 1);
        userRepository.save(user);

        subscriber.setSubscriptionsNumber(subscriber.getSubscriptionsNumber() + 1);
        userRepository.save(subscriber);

    }

    @Override
    public void unsubscribe(Long userId, Authentication authentication) {
        String email = authentication.getName();
        User subscriber = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (isSubscribed(subscriber, user)) {
            Subscription subscription = subscriptionRepository.findByUserAndSubscriber(user, subscriber);
            subscriptionRepository.delete(subscription);

            user.setSubscribersNumber(user.getSubscribersNumber() - 1);
            userRepository.save(user);

            subscriber.setSubscriptionsNumber(subscriber.getSubscriptionsNumber() - 1);
            userRepository.save(subscriber);
        } else {
            throw new RuntimeException("You are not subscribed to this user");
        }
    }

    @Override
    public List<ProfileResponseDTO> getSubscribers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        List<Subscription> subscriptionList = subscriptionRepository.findByUser(user);
        List<User> subscribers = subscriptionList.stream()
                .map(Subscription::getSubscriber)
                .collect(Collectors.toList());

        return subscribers.stream()
                .map(userMapper::userToUpdatedProfileDTO)
                .collect(Collectors.toList());
    }

    private boolean isSubscribed(User subscriber, User user) {
        Subscription subscription = subscriptionRepository.findByUserAndSubscriber(user, subscriber);
        return subscription != null;
    }
}
