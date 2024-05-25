package com.BlogifyHub.controller;

import com.BlogifyHub.service.ClapService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clap")
public class ClapController {
    private ClapService clapService;

    public ClapController(ClapService clapService) {
        this.clapService = clapService;
    }

    @PostMapping("/{id}")
    public void clap(@PathVariable(name = "id") Long postId, Authentication authentication){
        clapService.clap(postId,authentication);
    }

    @DeleteMapping("/{id}")
    public void removeUserClaps(@PathVariable(name = "id") Long postId, Authentication authentication){
        clapService.removeUserClaps(postId,authentication);
    }
}
