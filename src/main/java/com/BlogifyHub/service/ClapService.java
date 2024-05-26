package com.BlogifyHub.service;

import com.BlogifyHub.model.DTO.ProfileResponseDTO;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public interface ClapService {
    void clap(Long id, Authentication authentication);

    void removeUserClaps(Long Id, Authentication authentication);

    List<ProfileResponseDTO> getClappers(Long Id);


}
