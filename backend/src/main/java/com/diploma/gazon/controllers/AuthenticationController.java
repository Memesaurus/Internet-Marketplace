package com.diploma.gazon.controllers;

import com.diploma.gazon.DTO.request.AuthDTO;
import com.diploma.gazon.DTO.request.NewUserDTO;
import com.diploma.gazon.DTO.response.UserResponseDTO;
import com.diploma.gazon.exceptions.NotFoundException;
import com.diploma.gazon.exceptions.TokenExpiredException;
import com.diploma.gazon.models.User.User;
import com.diploma.gazon.services.RefreshTokenService;
import com.diploma.gazon.services.UserServices.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthenticationController {
    @Autowired
    private UserService userService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @GetMapping
    public List<UserResponseDTO> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/refresh")
    public void refreshJWTToken(HttpServletResponse response, HttpServletRequest request) {
        Cookie refreshTokenCookie = getRefreshTokenCookieOrThrow(request);

        try {
            String jwt = refreshTokenService.issueNewJwtToken(refreshTokenCookie);
            addAuthenticationCookie(response, jwt);
        } catch (TokenExpiredException e) {
            Cookie invalidatedRefreshTokenCookie = invalidateCookie(refreshTokenCookie);
            response.addCookie(invalidatedRefreshTokenCookie);

            throw e;
        }
    }

    private Cookie invalidateCookie(Cookie refreshTokenCookie) {
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setValue(null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);

        return refreshTokenCookie;
    }

    private Cookie getRefreshTokenCookieOrThrow(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("RefreshToken") && cookie.getValue() != null) {
                    return cookie;
                }
            }
        }

        throw new NotFoundException();
    }

    @PostMapping("/login")
    public void authenticate(@RequestBody AuthDTO authDTO, HttpServletResponse response) {
        String jwt = userService.authenticate(authDTO);

        if (authDTO.isRememberMe()) {
            String refreshToken = refreshTokenService.issueRefreshToken(authDTO.getUsername());
            addRefreshTokenCookie(refreshToken, response);
        }

        addAuthenticationCookie(response, jwt);
    }

    private void addRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
        Cookie cookie = new Cookie("RefreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(86400);
        cookie.setPath("/");

        response.addCookie(cookie);

    }

    private void addAuthenticationCookie(HttpServletResponse response, String jwt) {
        Cookie cookie = new Cookie("Authentication", jwt);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(1200);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    @PostMapping("/register")
    public String addUser(@RequestBody NewUserDTO newUserDTO) {
        return userService.addUser(newUserDTO);
    }
}
