package com.BlogifyHub.service;

import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.model.entity.VerificationToken;

public interface VerificationTokenService {
    VerificationToken createToken(User user);
    VerificationToken getToken(String token);
    void deleteToken(VerificationToken token);
}
