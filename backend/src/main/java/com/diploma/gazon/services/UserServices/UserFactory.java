package com.diploma.gazon.services.UserServices;

import com.diploma.gazon.DTO.request.NewUserDTO;
import com.diploma.gazon.exceptions.RoleNotAllowedException;
import com.diploma.gazon.models.AdministratorMember;
import com.diploma.gazon.models.CompanyMember;
import com.diploma.gazon.models.Member;
import com.diploma.gazon.models.User.User;
import com.diploma.gazon.models.User.UserCredentials;
import com.diploma.gazon.models.User.UserRole;

public interface UserFactory {
    static User getUser(UserRole role, NewUserDTO dto) {
        UserCredentials userCredentials = new UserCredentials(dto.getUsername(),
                dto.getPassword(),
                dto.getEmail(),
                dto.getName());
        return switch (role) {
            case ADMIN -> new AdministratorMember(userCredentials,
                    role,
                    dto.getAdditionalInfo());
            case MEMBER -> new Member(userCredentials,
                    role,
                    dto.getAdditionalInfo(),
                    dto.getSurname(),
                    dto.getMiddlename(),
                    dto.getAge());
            case COMPANY -> new CompanyMember(userCredentials,
                    role,
                    dto.getAdditionalInfo(),
                    dto.getName(),
                    dto.getDescription()
            );
        };
    }
}
