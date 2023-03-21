package com.diploma.gazon.models;

import com.diploma.gazon.models.User.User;
import com.diploma.gazon.models.User.UserAdditionalInfo;
import com.diploma.gazon.models.User.UserCredentials;
import com.diploma.gazon.models.User.UserRole;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "members")
public class AdministratorMember extends User {

    public AdministratorMember(UserCredentials rawUserCredentials,
                               UserRole userRole,
                               UserAdditionalInfo additionalInfo) {
        super(rawUserCredentials, userRole, additionalInfo);
    }
}
