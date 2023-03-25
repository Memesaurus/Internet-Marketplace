package com.diploma.gazon.services;

import com.diploma.gazon.config.jwt.JwtService;
import com.diploma.gazon.exceptions.NotFoundException;
import com.diploma.gazon.exceptions.TokenExpiredException;
import com.diploma.gazon.models.RefreshToken;
import com.diploma.gazon.models.User.User;
import com.diploma.gazon.repositories.RefreshTokenRepository;
import com.diploma.gazon.services.UserServices.UserService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    public String issueRefreshToken(String username) {
        User user = userService.getUserByUsername(username);

        RefreshToken refreshToken = new RefreshToken(user);

        refreshTokenRepository.findByUserId(user.getId()).ifPresent(refreshTokenRepository::delete);

        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    public String issueNewJwtToken(Cookie RefreshTokenCookie) {
        String value = RefreshTokenCookie.getValue();
        RefreshToken refreshToken = findByToken(value);

        if (!validateRefreshTokenCookie(refreshToken)) {
           throw new TokenExpiredException(HttpStatus.BAD_REQUEST, "Refresh token has expired");
        }

        User user = refreshToken.getUser();

        return jwtService.generateToken(user);
    }

    private Boolean validateRefreshTokenCookie(RefreshToken refreshToken) {

        if (isRefreshTokenExpired(refreshToken)) {
            refreshTokenRepository.delete(refreshToken);
            return false;
        }
        return true;
    }

    private boolean isRefreshTokenExpired(RefreshToken refreshToken) {
        return Instant.now().isAfter(refreshToken.getExpiration());
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(NotFoundException::new);
    }
}
