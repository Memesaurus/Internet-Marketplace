package com.diploma.gazon.services;

import com.diploma.gazon.DTO.AuthDTO;
import com.diploma.gazon.config.jwt.JwtService;
import com.diploma.gazon.exceptions.AlreadyExistsException;
import com.diploma.gazon.exceptions.NotFoundException;
import com.diploma.gazon.exceptions.RoleNotAllowedException;
import com.diploma.gazon.models.User.User;
import com.diploma.gazon.models.User.UserRole;
import com.diploma.gazon.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService<T extends User> {
    @Autowired
    private UserRepository<T> userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public List<T> getAllUsers() {
        return userRepository.findAll();
    }

    public T getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(NotFoundException::new);
    }

    public User getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        return (User) authentication.getPrincipal();
    }

    public String addUser(T user, UserRole role) {
        if (Boolean.TRUE.equals(isUserRoleAdmin(user.getUserRole()))) {
            throw new RoleNotAllowedException();
        }

        try {
            user.encodePassword();
            // TODO add mail confirmation
            user.setIsEnabled(true);
            user.setUserRole(role);

            userRepository.save(user);
        } catch (DuplicateKeyException exception) {
            throw new AlreadyExistsException("User already exists");
        }

        return jwtService.generateToken(user);
    }

    public Boolean isUserRoleAdmin(UserRole userRole) {
        return userRole == UserRole.ADMIN;
    }

    public String authenticate(AuthDTO authDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authDTO.username,
                        authDTO.password
                )
        );

        User authenticatedUser = userRepository.findByUsername(authDTO.username)
                .orElseThrow(NotFoundException::new);

        return jwtService.generateToken(authenticatedUser);
    }

}
