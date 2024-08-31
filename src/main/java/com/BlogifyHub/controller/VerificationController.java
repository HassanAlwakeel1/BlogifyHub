package com.BlogifyHub.controller;

import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.model.entity.VerificationToken;
import com.BlogifyHub.repository.UserRepository;
import com.BlogifyHub.service.VerificationTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Verification")
public class VerificationController {

    @Autowired
    private VerificationTokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        VerificationToken verificationToken = tokenService.getToken(token);
        if (verificationToken == null) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        tokenService.deleteToken(verificationToken);

        return ResponseEntity.ok("Account verified successfully");
    }
}
