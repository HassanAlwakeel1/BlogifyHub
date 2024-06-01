package com.BlogifyHub.service;

import com.BlogifyHub.model.DTO.JwtAuthenticationResponse;
import com.BlogifyHub.model.DTO.RefreshTokenRequest;
import com.BlogifyHub.model.DTO.SignInRequest;
import com.BlogifyHub.model.DTO.SignUpRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signin(SignInRequest signinRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    void initiatePasswordReset(String email);

    void resetPassword(String token, String newPassword);
}
