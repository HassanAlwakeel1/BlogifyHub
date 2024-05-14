package com.BlogifyHub.service.impl;

import com.BlogifyHub.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class LogoutHandlerImpl implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if(!StringUtils.hasText(authHeader) ||
                !org.apache.commons.lang3.StringUtils.startsWith(authHeader,"Bearer ")){
            return;
        }

        jwt = authHeader.substring(7);

        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);
        if(storedToken != null){
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        }

    }
}

