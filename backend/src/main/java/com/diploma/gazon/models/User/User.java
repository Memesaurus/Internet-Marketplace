package com.diploma.gazon.models.User;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "members")
public abstract class User implements UserDetails {
    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    private String name;
    private Boolean isEnabled;
    private UserRole userRole;

    private UserAdditionalInfo additionalInfo;

    protected User(UserCredentials credentials,
                    UserRole userRole,
                    UserAdditionalInfo additionalInfo) {
        this(credentials.getUsername(),
                credentials.getPassword(),
                credentials.getEmail(),
                credentials.getName(),
                userRole,
                additionalInfo);
    }

    protected User(String username,
                String password,
                String email,
                String name,
                UserRole userRole,
                UserAdditionalInfo additionalInfo) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.isEnabled = true;
        this.userRole = userRole;
        this.additionalInfo = additionalInfo;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isEnabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isEnabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isEnabled;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    public boolean isAdmin() {
        return this.userRole.equals(UserRole.ADMIN);
    }
}
