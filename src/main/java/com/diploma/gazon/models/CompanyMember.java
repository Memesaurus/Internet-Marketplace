package com.diploma.gazon.models;

import com.diploma.gazon.models.User.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "members")
public class CompanyMember extends User {
    private String companyName;
    private String companyDescription;
}
