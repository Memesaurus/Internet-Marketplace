package com.diploma.gazon.DTO.response;

import com.diploma.gazon.models.User.UserAdditionalInfo;
import lombok.Data;

@Data
public class UserResponseDTO {
    String username;
    String email;
    UserAdditionalInfo additionalInfo;
    String name;
}
