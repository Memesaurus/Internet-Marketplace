package com.diploma.gazon.models;

import com.diploma.gazon.models.User.User;
import com.diploma.gazon.models.User.UserAdditionalInfo;
import com.diploma.gazon.models.User.UserCredentials;
import com.diploma.gazon.models.User.UserRole;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "members")
public class Member extends User {
    private String name;
    private String surname;
    private String middleName;
    private List<Item> memberCart;
    private Integer age;

    public Member(UserCredentials rawUserCredentials,
                  UserRole userRole,
                  UserAdditionalInfo additionalInfo,
                  String name,
                  String surname,
                  String middleName,
                  Integer age) {
        super(rawUserCredentials, userRole, additionalInfo);
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
        this.age = age;
        this.memberCart = new ArrayList<Item>();
    }

    public String getFullName() {
        return String.join(" ", this.surname, this.name, this.middleName);
    }
}
