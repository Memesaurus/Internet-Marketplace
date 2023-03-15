package com.diploma.gazon.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "members")
public class User extends Member {
    private String name;
    private String surname;
    private String middleName;
    private Integer age;

    public String getFullName() {
        return String.join(" ", this.surname, this.name, this.middleName);
    }
}
