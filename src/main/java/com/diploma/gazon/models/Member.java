package com.diploma.gazon.models;

import com.diploma.gazon.models.User.User;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "members")
public class Member extends User {
    private String name;
    private String surname;
    private String middleName;
    private Integer age;

    public String getFullName() {
        return String.join(" ", this.surname, this.name, this.middleName);
    }
}
