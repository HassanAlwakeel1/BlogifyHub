package com.BlogifyHub.service;

import org.springframework.security.core.Authentication;

public interface ClapService {
    void clap(Long id, Authentication authentication);

    void removeUserClaps(Long Id, Authentication authentication);


}
