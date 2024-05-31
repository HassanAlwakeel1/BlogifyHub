package com.BlogifyHub.service.impl;

import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.model.entity.VerificationToken;
import com.BlogifyHub.repository.VerificationTokenRepository;
import com.BlogifyHub.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Override
    public VerificationToken createToken(User user) {
        VerificationToken token = new VerificationToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusDays(1)); // Token valid for 1 day
        return tokenRepository.save(token);
    }

    @Override
    public VerificationToken getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void deleteToken(VerificationToken token) {
        tokenRepository.delete(token);
    }
}
