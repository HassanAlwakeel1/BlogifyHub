package com.BlogifyHub.controller;

import com.BlogifyHub.service.MuteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mutes")
public class MuteController {
    private MuteService muteService;

    public MuteController(MuteService muteService) {
        this.muteService = muteService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> muteUser(@PathVariable(name = "id") long mutedUserId,
                                           Authentication authentication){
        return ResponseEntity.ok(muteService.muteUser(mutedUserId,authentication));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> unMuteUser(@PathVariable(name = "id") long mutedUserId,
                                             Authentication authentication) {
        return ResponseEntity.ok(muteService.unMuteUser(mutedUserId,authentication));
    }
}
