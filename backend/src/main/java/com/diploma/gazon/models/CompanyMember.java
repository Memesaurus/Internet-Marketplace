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
public class CompanyMember extends User {
    private String companyName;
    private String companyDescription;

    public CompanyMember(UserCredentials rawUserCredentials,
                         UserRole userRole,
                         UserAdditionalInfo additionalInfo,
                         String companyName,
                         String companyDescription) {
        super(rawUserCredentials, userRole, additionalInfo);
        this.companyName = companyName;
        this.companyDescription = companyDescription;
    }
}
