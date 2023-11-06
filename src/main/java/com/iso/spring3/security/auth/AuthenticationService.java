package com.iso.spring3.security.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.iso.spring3.core.utils.business.BusinessRule;
import com.iso.spring3.core.utils.business.CustomModelMapper;
import com.iso.spring3.core.utils.constants.CoreConstants;
import com.iso.spring3.core.utils.result.ErrorResult;
import com.iso.spring3.core.utils.result.Result;
import com.iso.spring3.core.utils.result.SuccessDataResult;
import com.iso.spring3.core.utils.result.SuccessResult;
import com.iso.spring3.security.config.JwtService;
import com.iso.spring3.security.models.Token;
import com.iso.spring3.security.models.TokenType;
import com.iso.spring3.security.models.User;
import com.iso.spring3.security.repo.TokenRepository;
import com.iso.spring3.security.repo.UserRepository;
import com.kim.dibt.core.utils.result.*;
import com.iso.spring3.models.PersonalUser;
import com.iso.spring3.repo.PersonalUserRepo;
import com.iso.spring3.security.dto.AuthenticationResponse;
import com.iso.spring3.security.dto.LoginRequest;
import com.iso.spring3.security.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PersonalUserRepo personalUserRepo;
    private final CustomModelMapper modelMapper;

    public Result register(RegisterRequest request) {
        var result = BusinessRule.run(
                checkUsernameExists(request.getUsername()),
                checkEmailExists(request.getEmail())
        );
        if (result != null) {
            return result;
        }
        PersonalUser personalUser = modelMapper.ofStandard().map(request, PersonalUser.class);
        personalUser.setPassword(passwordEncoder.encode(request.getPassword()));
        PersonalUser savedUser = personalUserRepo.save(personalUser);


        var accessToken = jwtService.generateAccessToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);
        saveUserToken(savedUser, accessToken);

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return new SuccessDataResult<>(authenticationResponse, CoreConstants.REGISTRATION_SUCCESS);
    }

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, accessToken);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = userRepository.findByUsername(username).orElseThrow();

            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateAccessToken(user);
                saveUserToken(user, accessToken);
                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public Result changePassword(String oldPassword, String newPassword, HttpServletRequest request) {
        String username = jwtService.extractUsername(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7));
        var user = userRepository.findByUsername(username).orElse(null);
        if (user == null)
            return ErrorResult.of(CoreConstants.AUTHENTICATION_FAILED);
        if (!passwordEncoder.matches(oldPassword, user.getPassword()))
            return ErrorResult.of(CoreConstants.WRONG_PASSWORD);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        revokeAllUserTokens(user);
        return SuccessResult.of(CoreConstants.PASSWORD_CHANGED);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private Result checkUsernameExists(String username) {
        var user = userRepository.findByUsername(username);
        if (user.isPresent())
            return ErrorResult.of(CoreConstants.USERNAME_ALREADY_EXISTS);
        return SuccessResult.of(CoreConstants.NOT_FOUND);
    }

    private Result checkEmailExists(String email) {
        var user = userRepository.findByEmail(email);
        if (user.isPresent())
            return ErrorResult.of(CoreConstants.EMAIL_ALREADY_EXISTS);
        return SuccessResult.of(CoreConstants.NOT_FOUND);
    }

}
