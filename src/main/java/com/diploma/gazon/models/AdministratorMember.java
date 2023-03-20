package com.diploma.gazon.models;

import com.diploma.gazon.models.User.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "members")
public class AdministratorMember extends User {
}
