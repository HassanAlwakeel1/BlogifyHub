package com.BlogifyHub.repository;

import com.BlogifyHub.model.entity.Mute;
import com.BlogifyHub.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MuteRepository extends JpaRepository<Mute,Long> {
    @Query("SELECT f FROM Mute f WHERE (f.user = :user AND f.mutedUser = :mutedUser)")
    Mute findByUserAndMutedUser(@Param("user") User user,@Param("mutedUser") User mutedUser);

    List<Mute> findByUser(User user);
}
