package com.BlogifyHub.service.impl;

import com.BlogifyHub.model.DTO.JwtAuthenticationResponse;
import com.BlogifyHub.model.DTO.RefreshTokenRequest;
import com.BlogifyHub.model.DTO.SignInRequest;
import com.BlogifyHub.model.DTO.SignUpRequest;
import com.BlogifyHub.model.entity.Token;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.model.entity.VerificationToken;
import com.BlogifyHub.model.entity.enums.TokenType;
import com.BlogifyHub.repository.TokenRepository;
import com.BlogifyHub.repository.UserRepository;
import com.BlogifyHub.service.AuthenticationService;
import com.BlogifyHub.service.JWTService;
import com.BlogifyHub.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    private final TokenRepository tokenRepository;

    private final VerificationTokenService tokenService;

    private final JavaMailSender mailSender;

    public JwtAuthenticationResponse signup(SignUpRequest signUpRequest){
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setRole(signUpRequest.getRole());
        user.setBio(signUpRequest.getBio());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setEnabled(false); // Set user as not enabled

        var savedUser = userRepository.save(user);

        // Create verification token
        VerificationToken token = tokenService.createToken(savedUser);

        // Send verification email
        sendVerificationEmail(savedUser, token.getToken());
        var jwtToken = jwtService.generateToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(new HashMap<>(),user);
        saveUserToken(savedUser, jwtToken);

        return JwtAuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }
    private void sendVerificationEmail(User user, String token) {
        String url = "http://localhost:8080/api/v1/auth/verify?token=" + token;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Verify your email");
        email.setText("Click the following link to verify your email: " + url);
        mailSender.send(email);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    public JwtAuthenticationResponse signin(SignInRequest signinRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),
                signinRequest.getPassword()));

        var user=userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(()-> new IllegalArgumentException("Invalid Username or password"));

        var jwt = jwtService.generateToken(user);

        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        revokeAllUserTokens(user);
        saveUserToken(user, jwt);

        return jwtAuthenticationResponse;
    }

    public void revokeAllUserTokens(User user){
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if(validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(
                token -> {
                    token.setExpired(true);
                    token.setRevoked(true);
                }
        );
        tokenRepository.saveAll(validUserTokens);
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest)  {
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        if(jwtService.isTokenValid(refreshTokenRequest.getToken(),user)){
            var jwt = jwtService.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());

            return jwtAuthenticationResponse;
        }
        return null;
    }
}

