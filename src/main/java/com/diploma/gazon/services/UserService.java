package com.diploma.gazon.services;

import com.diploma.gazon.DTO.AuthDTO;
import com.diploma.gazon.DTO.NewUserDTO;
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
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(NotFoundException::new);
    }

    public User getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        return (User) authentication.getPrincipal();
    }

    public String addUser(NewUserDTO newUserDTO, UserRole role) {
        if (isUserRoleAdmin(role) && isLoggedInUserAdmin()) {
            throw new RoleNotAllowedException();
        }

        //TODO add mail confirmation
        User newUser = UserFactory.getUser(role, newUserDTO);

        try {
            userRepository.save(newUser);
        } catch (DuplicateKeyException exception) {
            throw new AlreadyExistsException("User already exists");
        }

        return jwtService.generateToken(newUser);
    }

    private Boolean isLoggedInUserAdmin() {
        if (getCurrentUser() == null) {
            return false;
        }

        return isUserRoleAdmin(getCurrentUser().getUserRole());
    }

    public Boolean isUserRoleAdmin(UserRole userRole) {
        return userRole == UserRole.ADMIN;
    }

    public String authenticate(AuthDTO authDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authDTO.getUsername(),
                        authDTO.getPassword()
                )
        );

        User authenticatedUser = userRepository.findByUsername(authDTO.getUsername())
                .orElseThrow(NotFoundException::new);

        return jwtService.generateToken(authenticatedUser);
    }

}
