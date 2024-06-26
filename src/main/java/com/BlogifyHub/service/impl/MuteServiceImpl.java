package com.BlogifyHub.service.impl;

import com.BlogifyHub.exception.ResourceNotFoundException;
import com.BlogifyHub.model.DTO.ProfileResponseDTO;
import com.BlogifyHub.model.entity.Mute;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.model.mapper.UserMapper;
import com.BlogifyHub.repository.MuteRepository;
import com.BlogifyHub.repository.UserRepository;
import com.BlogifyHub.service.MuteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MuteServiceImpl implements MuteService {
    private UserRepository userRepository;
    private MuteRepository muteRepository;

    private UserMapper userMapper;

    public MuteServiceImpl(UserRepository userRepository, MuteRepository muteRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.muteRepository = muteRepository;
        this.userMapper = userMapper;
    }

    @Override
    public String muteUser(long mutedUserId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("Invalid token"));

        User mutedUser = userRepository.findById(mutedUserId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",mutedUserId));

        if (user.equals(mutedUser)){
            throw new RuntimeException("You can't mute yourself");
        }

        if (isMuted(user,mutedUser)){
            throw new RuntimeException("You can't follow the same person twice, you already following him");
        }

        Mute mute = new Mute();
        mute.setUser(user);
        mute.setMutedUser(mutedUser);
        muteRepository.save(mute);
        return "You muted " + mutedUser.getFirstName() + " " + mutedUser.getLastName() + " successfully";
    }

    @Override
    public String unMuteUser(long mutedUserId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("Invalid token"));

        User mutedUser = userRepository.findById(mutedUserId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",mutedUserId));

        if (isMuted(user,mutedUser)){
            Mute mute = muteRepository.findByUserAndMutedUser(user,mutedUser);
            muteRepository.delete(mute);
        }
        return "You unMuted " + mutedUser.getFirstName() + " " + mutedUser.getLastName() + " Successfully" ;
    }

    @Override
    public List<ProfileResponseDTO> getMutedUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("Invalid token"));
        List<Mute> muteList = muteRepository.findByUser(user);
        List<User> mutedUsers = muteList.stream()
                .map(Mute::getMutedUser)
                .collect(Collectors.toList());
        List<ProfileResponseDTO> profileResponseDTOS = mutedUsers.stream()
                .map(userMapper::userToUpdatedProfileDTO)
                .collect(Collectors.toList());
        return profileResponseDTOS;
    }

    private boolean isMuted(User user, User mutedUser){
        Mute mute = muteRepository.findByUserAndMutedUser(user,mutedUser);
        if (mute == null){
            return false;
        }
        return true;
    }
}
