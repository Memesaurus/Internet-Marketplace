package com.diploma.gazon.mappers;

import com.diploma.gazon.DTO.response.UserResponseDTO;
import com.diploma.gazon.DTO.response.UserStateDTO;
import com.diploma.gazon.models.User.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toUserResponseDto(User user);

    List<UserResponseDTO> toUserResponseDto(Iterable<User> users);

    UserStateDTO toUserStateDto(User user);
}
