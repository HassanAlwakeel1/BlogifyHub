package com.BlogifyHub.controller;

import com.BlogifyHub.service.ClapService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clap")
public class ClapController {
    private final ClapService commentClapService;
    private final ClapService postClapService;

    public ClapController(
            @Qualifier("commentClapService") ClapService commentClapService,
            @Qualifier("postClapService") ClapService postClapService) {
        this.commentClapService = commentClapService;
        this.postClapService = postClapService;
    }
    @PostMapping("/{id}")
    public void clapToPost(@PathVariable(name = "id") Long postId, Authentication authentication){
        postClapService.clap(postId,authentication);
    }

    @DeleteMapping("/{id}")
    public void removeUserPostClaps(@PathVariable(name = "id") Long postId, Authentication authentication){
        postClapService.removeUserClaps(postId,authentication);
    }

    @PostMapping("/comment/{id}")
    public void clapToComment(@PathVariable(name = "id") Long commentId, Authentication authentication){
        commentClapService.clap(commentId,authentication);
    }

    @DeleteMapping("/comment/{id}")
    public void removeUserCommentClaps(@PathVariable(name = "id") Long commentId, Authentication authentication){
        commentClapService.removeUserClaps(commentId,authentication);
    }
}
