package com.diploma.gazon.services.UserServices;

import com.diploma.gazon.DTO.request.AuthDTO;
import com.diploma.gazon.DTO.request.NewUserDTO;
import com.diploma.gazon.DTO.response.UserResponseDTO;
import com.diploma.gazon.DTO.response.UserStateDTO;
import com.diploma.gazon.config.jwt.JwtService;
import com.diploma.gazon.exceptions.AlreadyExistsException;
import com.diploma.gazon.exceptions.NotFoundException;
import com.diploma.gazon.exceptions.RoleNotAllowedException;
import com.diploma.gazon.exceptions.UnauthorizedException;
import com.diploma.gazon.mappers.UserMapper;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return userMapper.toUserResponseDto(users);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(NotFoundException::new);
    }

    public String addUser(NewUserDTO newUserDTO) {
        UserRole role = newUserDTO.getRole();

        if (role == UserRole.ADMIN && isLoggedInUserAdmin()) {
            throw new RoleNotAllowedException();
        }

        encodeNewUserPassword(newUserDTO);

        User newUser = UserFactory.getUser(role, newUserDTO);

        try {
            userRepository.save(newUser);
        } catch (DuplicateKeyException exception) {
            throw new AlreadyExistsException("User already exists");
        }

        return jwtService.generateToken(newUser);
    }

    private void encodeNewUserPassword(NewUserDTO newUserDTO) {
        newUserDTO.setPassword(passwordEncoder.encode(newUserDTO.getPassword()));
    }

    private Boolean isLoggedInUserAdmin() {
        User currentUser = getCurrentUser();

        if (currentUser == null) {
            return false;
        }

        return currentUser.isAdmin();
    }

    public UserStateDTO getCurrentUserState() {
        User currentUser = getCurrentUser();

        return userMapper.toUserStateDto(currentUser);
    }

    public User getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication.getPrincipal() instanceof String) {
            throw new UnauthorizedException();
        }

        return (User) authentication.getPrincipal();
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
