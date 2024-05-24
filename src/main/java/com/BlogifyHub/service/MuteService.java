package com.BlogifyHub.service;

import com.BlogifyHub.model.DTO.ProfileResponseDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MuteService {
    String muteUser(long mutedUserId, Authentication authentication);

    String unMuteUser(long mutedUserId, Authentication authentication);

    List<ProfileResponseDTO> getMutedUser(Authentication authentication);
}
