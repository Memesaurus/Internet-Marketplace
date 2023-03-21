package com.diploma.gazon.models.User;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Document(collection = "members")
public abstract class User implements UserDetails {
    public static final PasswordEncoder encoder =
            PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    private Boolean isEnabled;
    private UserRole userRole;

    private UserAdditionalInfo additionalInfo;

    protected User(UserCredentials credentials,
                    UserRole userRole,
                    UserAdditionalInfo additionalInfo) {
        this(credentials.getUsername(),
                credentials.getPassword(),
                credentials.getEmail(),
                userRole,
                additionalInfo);
    }

    protected User(String username,
                String password,
                String email,
                UserRole userRole,
                UserAdditionalInfo additionalInfo) {
        this.username = username;
        this.password = encodePassword(password);
        this.email = email;
        this.isEnabled = true;
        this.userRole = userRole;
        this.additionalInfo = additionalInfo;
    }

    private String encodePassword(String password) {
        return encoder.encode(password);
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
}
