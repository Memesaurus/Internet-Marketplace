package com.diploma.gazon.DTO.request;

import com.diploma.gazon.models.User.UserAdditionalInfo;
import com.diploma.gazon.models.User.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NewUserDTO extends AuthDTO {
    private String email;
    private UserAdditionalInfo additionalInfo;
    private UserRole role;

    //Shared user data
    private String name;
    //Company user data
    private String description;
    //Member user data
    private String middlename;
    private String surname;
    private Integer age;
}
