package com.BlogifyHub.security;

import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ActiveAccountAspect {

    private final UserRepository userRepository;

    @Before("@annotation(com.BlogifyHub.security.RequiresActiveAccount)")
    public void checkIfUserIsActive() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            if (!user.isEnabled()) {
                throw new IllegalStateException("Account not activated. Please verify your email.");
            }
        }
    }
}
