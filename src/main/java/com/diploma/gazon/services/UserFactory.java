package com.diploma.gazon.services;

import com.diploma.gazon.DTO.NewUserDTO;
import com.diploma.gazon.exceptions.RoleNotAllowedException;
import com.diploma.gazon.models.AdministratorMember;
import com.diploma.gazon.models.CompanyMember;
import com.diploma.gazon.models.Member;
import com.diploma.gazon.models.User.User;
import com.diploma.gazon.models.User.UserCredentials;
import com.diploma.gazon.models.User.UserRole;

public interface UserFactory {
    static User getUser(UserRole role, NewUserDTO dto) {
        UserCredentials userCredentials = new UserCredentials(dto.getUsername(), dto.getPassword(), dto.getEmail());
        switch (role) {
            case ADMIN -> {
                return new AdministratorMember(userCredentials,
                        role,
                        dto.getUserAdditionalInfo());
            }
            case MEMBER -> {
                return new Member(userCredentials,
                        role,
                        dto.getUserAdditionalInfo(),
                        dto.getName(),
                        dto.getSurname(),
                        dto.getMiddlename(),
                        dto.getAge());
            }
            case COMPANY -> {
                return new CompanyMember(userCredentials,
                        role,
                        dto.getUserAdditionalInfo(),
                        dto.getName(),
                        dto.getDescription()
                );
            }
            default -> throw new RoleNotAllowedException();
        }
    }
}
