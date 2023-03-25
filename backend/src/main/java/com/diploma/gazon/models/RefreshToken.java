package com.diploma.gazon.models;

import com.diploma.gazon.models.User.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Document(collection = "refresh_tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    private static final Integer EXPIRATION_TIME = 86400; // 24 hours

    @Id
    private String id;
    private String token;
    private Instant created;
    private Instant expiration;
    @DBRef
    private User user;

    public RefreshToken(User user) {
        this.token = UUID.randomUUID().toString();
        this.created = Instant.now();
        this.expiration = Instant.now().plusSeconds(EXPIRATION_TIME);
        this.user = user;
    }
}
