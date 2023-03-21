package com.diploma.gazon.DTO;

import com.diploma.gazon.models.User.UserAdditionalInfo;
import lombok.Data;

@Data
public class NewUserDTO extends AuthDTO {
    private String email;
    private UserAdditionalInfo userAdditionalInfo;
    private String name;
    private String description;
    private String middlename;
    private String surname;
    private Integer age;
}
