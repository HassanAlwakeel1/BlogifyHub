package com.BlogifyHub.service.impl;

import com.BlogifyHub.exception.ResourceNotFoundException;
import com.BlogifyHub.model.DTO.ProfileResponseDTO;
import com.BlogifyHub.model.entity.Post;
import com.BlogifyHub.model.entity.PostClap;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.repository.PostClapRepository;
import com.BlogifyHub.repository.PostRepository;
import com.BlogifyHub.repository.UserRepository;
import com.BlogifyHub.service.ClapService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Qualifier("postClapService")
public class PostClapServiceImpl implements ClapService {
    private UserRepository userRepository;

    private PostRepository postRepository;

    private PostClapRepository postClapRepository;

    public PostClapServiceImpl(UserRepository userRepository,
                               PostRepository postRepository,
                               PostClapRepository postClapRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.postClapRepository = postClapRepository;
    }

    @Override
    public void clap(Long postId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(email));
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));

        if (user.equals(post.getUser())){
            throw new RuntimeException("You can't clap to your own post.");
        }

        Optional<PostClap> optionalClap = postClapRepository.findByUserAndPost(user,post);
        PostClap clap;
        if (optionalClap.isPresent()){
            clap = optionalClap.get();
            if (clap.getNumberOfClaps() >= 50) {
                throw new RuntimeException("You can't clap more than 50 times on the same post.");
            }
            clap.setNumberOfClaps(clap.getNumberOfClaps() + 1);
        }else {
            clap = new PostClap();
            clap.setUser(user);
            clap.setPost(post);
            clap.setNumberOfClaps(1);
            post.getPostClaps().add(clap);
            post.setNumberOfClappers(post.getNumberOfClappers() + 1);
        }
        post.setNumberOfClaps(post.getNumberOfClaps() + 1);
        postRepository.save(post);
        postClapRepository.save(clap);
    }

    @Override
    public void removeUserClaps(Long postId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(email));
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));

        Optional<PostClap> optionalClap = postClapRepository.findByUserAndPost(user,post);
        PostClap clap;
        if (optionalClap.isPresent()){
            clap = optionalClap.get();
            post.setNumberOfClaps(post.getNumberOfClaps() - clap.getNumberOfClaps());
            post.setNumberOfClappers(post.getNumberOfClappers() - 1);
            postClapRepository.delete(clap);
            postRepository.save(post);
        }else throw new RuntimeException("Clap not found");
    }

    @Override
    public List<ProfileResponseDTO> getClappers(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","postId",postId));
        List<PostClap> postClaps = new ArrayList<>();
        postClaps = post.getPostClaps();
        List<User> users = postClaps.stream()
                .map(PostClap::getUser)
                .collect(Collectors.toList());
        List<ProfileResponseDTO> profileResponseDTOS = users.stream()
                .map(this::mapToProfileResponseDTO)
                .collect(Collectors.toList());

        return profileResponseDTOS;
    }

    private ProfileResponseDTO mapToProfileResponseDTO(User user) {
        return new ProfileResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBio(),
                user.getProfilePictureURL(),
                user.getFollowersNumber()
        );
    }


}
