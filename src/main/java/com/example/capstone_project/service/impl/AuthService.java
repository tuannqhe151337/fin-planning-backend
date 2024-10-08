package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.UserDetail;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.redis.UserDetailRepository;
import com.example.capstone_project.utils.helper.JwtHelper;
import com.example.capstone_project.entity.Authority;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.repository.AuthorityRepository;
import com.example.capstone_project.repository.redis.LogoutTokenRepository;
import com.example.capstone_project.repository.UserRepository;
import com.example.capstone_project.service.result.LoginResult;
import com.example.capstone_project.service.result.TokenPair;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${application.security.access-token.expiration}")
    private long ACCESS_TOKEN_EXPIRATION;

    private final AuthenticationProvider authenticationProvider;

    private final JwtHelper jwtHelper;

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final LogoutTokenRepository logoutTokenRepository;

    private final UserAuthorityRepository userAuthorityRepository;

    private final UserDetailRepository userDetailRepository;

    public LoginResult login(String email, String password) throws Exception {
        // Login using authentication provider
        Authentication usernamePasswordAuthenticationToken = this.authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        if (usernamePasswordAuthenticationToken == null || usernamePasswordAuthenticationToken.getPrincipal() == null) {
            throw new Exception();
        }

        // Get user and all authorities
        int userId = Integer.parseInt(usernamePasswordAuthenticationToken.getPrincipal().toString());
        final User user = this.getDetailedUserByIdFromDatabase(userId);

        // Save to redis
        this.saveUserAuthoritiesToRedis(userId, user.getAuthorities());
        this.saveUserDetailToRedis(userId, UserDetail.builder()
                        .departmentId(user.getDepartment().getId())
                        .roleCode(user.getRole().getCode())
                        .build()
        );

        // Generate token
        String accessToken = this.jwtHelper.generateAccessToken(userId);
        String refreshToken = this.jwtHelper.generateRefreshToken(userId);

        return LoginResult.builder()
                .tokenPair(
                        TokenPair.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .build()
                )
                .user(user)
                .build();
    }

    public TokenPair refreshToken(String accessToken, String refreshToken) throws Exception {
        // Check if accessToken and refreshToken belong to the same user
        // Extract userId from token and compare
        final int userIdFromToken = this.jwtHelper.extractUserIdFromExpiredAccessToken(accessToken);
        final int userIdFromRefreshToken = this.jwtHelper.extractUserIdFromRefreshToken(refreshToken);

        if (userIdFromToken != userIdFromRefreshToken) {
            throw new Exception();
        }

        // Get user and authorities and role and department
        final User user = this.getUserWithRoleAndDepartmentAndAuthoritiesByIdFromDatabase(userIdFromToken);

        // Save to redis
        this.saveUserAuthoritiesToRedis(user.getId(), user.getAuthorities());
        this.saveUserDetailToRedis(user.getId(), UserDetail.builder()
                .departmentId(user.getDepartment().getId())
                .roleCode(user.getRole().getCode())
                .build()
        );

        // Generate token
        TokenPair tokenPair = TokenPair.builder()
                .accessToken(this.jwtHelper.generateAccessToken(userIdFromToken))
                .refreshToken(this.jwtHelper.generateRefreshToken(userIdFromToken))
                .build();

        return TokenPair.builder()
                .accessToken(tokenPair.getAccessToken())
                .refreshToken(tokenPair.getRefreshToken())
                .build();
    }

    public void logout(String accessToken) {
        accessToken = accessToken.substring(7); // Remove Bearer

        // Remove user authorities and data from redis
        final int userId = this.jwtHelper.extractUserIdFromAccessToken(accessToken);
        this.userAuthorityRepository.delete(userId);
        this.userDetailRepository.delete(userId);

        // Add the accessToken into "blacklist"
        this.logoutTokenRepository.save(accessToken, Duration.ofMillis(ACCESS_TOKEN_EXPIRATION));
    }

    public User getDetailedUserByIdFromDatabase(int userId) throws Exception {
        // Get user
        Optional<User> userOptional = this.userRepository.findActiveUserDetailedById((long) userId);
        if (userOptional.isEmpty()) {
            throw new Exception("User does not exists!");
        }

        final User user = userOptional.get();

        if (user.getRole() == null || user.getDepartment() == null || user.getPosition() == null) {
            throw new Exception("Role or department or position is empty!");
        }

        // Get authorities of user
        final List<Authority> authorities = this.authorityRepository.findAuthoritiesOfRole(user.getRole().getId());
        user.setAuthorities(authorities);

        // Attach authorities into user
        user.setAuthorities(authorities);

        return user;
    }

    private User getUserWithRoleAndDepartmentAndAuthoritiesByIdFromDatabase(long userId) throws Exception {
        // Get user
        Optional<User> userOptional = this.userRepository.findActiveUserWithRoleAndDepartmentById(userId);
        if (userOptional.isEmpty()) {
            throw new Exception("User does not exists!");
        }

        final User user = userOptional.get();

        if (user.getRole() == null || user.getDepartment() == null) {
            throw new Exception("Role or department is empty!");
        }

        // Get authorities of user
        final List<Authority> authorities = this.authorityRepository.findAuthoritiesOfRole(user.getRole().getId());
        user.setAuthorities(authorities);

        // Attach authorities into user
        user.setAuthorities(authorities);

        return user;
    }

    private void saveUserAuthoritiesToRedis(long userId, List<Authority> authorities) {
        List<String> authoritiesCodes = new ArrayList<>();
        for (Authority authority: authorities) {
            authoritiesCodes.add(authority.getCode().getValue());
        }

        this.userAuthorityRepository.save(userId, authoritiesCodes, Duration.ofMillis(ACCESS_TOKEN_EXPIRATION));
    }

    private void saveUserDetailToRedis(long userId, UserDetail userDetail) {
        this.userDetailRepository.save(userId, userDetail, Duration.ofMillis(ACCESS_TOKEN_EXPIRATION));
    }
}
