package com.diploma.gazon.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public abstract class Member {
    @Id
    private String id;
    private String username;
    private String email;
    private MemberAdditionalInfo additionalInfo;
}
