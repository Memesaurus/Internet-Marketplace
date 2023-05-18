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
    private String surname;
    private String middlename;
    private List<Item> memberCart;
    private Integer age;

    public Member(UserCredentials rawUserCredentials,
                  UserRole userRole,
                  UserAdditionalInfo additionalInfo,
                  String surname,
                  String middlename,
                  Integer age) {
        super(rawUserCredentials, userRole, additionalInfo);
        this.surname = surname;
        this.middlename = middlename;
        this.age = age;
        this.memberCart = new ArrayList<Item>();
    }

    public String getFullName() {
        return String.join(" ", this.surname, this.middlename);
    }
}
