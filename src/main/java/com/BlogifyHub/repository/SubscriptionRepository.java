package com.BlogifyHub.repository;

import com.BlogifyHub.model.entity.Subscription;
import com.BlogifyHub.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    @Query("SELECT s FROM Subscription s WHERE s.user = :user AND s.subscriber = :subscriber")
    Subscription findByUserAndSubscriber(@Param("user") User user, @Param("subscriber") User subscriber);

    @Query("SELECT s FROM Subscription s WHERE s.user = :user")
    List<Subscription> findByUser(@Param("user") User user);

    @Query("SELECT s FROM Subscription s WHERE s.subscriber = :subscriber")
    List<Subscription> findBySubscriber(@Param("subscriber") User subscriber);
}
