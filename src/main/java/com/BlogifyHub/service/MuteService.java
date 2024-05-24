package com.BlogifyHub.service;

import org.springframework.security.core.Authentication;

public interface MuteService {
    String muteUser(long mutedUserId, Authentication authentication);

    String unMuteUser(long mutedUserId, Authentication authentication);
}
