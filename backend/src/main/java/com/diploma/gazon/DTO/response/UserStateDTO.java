package com.diploma.gazon.DTO.response;

import com.diploma.gazon.DTO.request.CartItemDTO;
import com.diploma.gazon.models.CartItem;
import com.diploma.gazon.models.User.UserRole;
import lombok.Data;

import java.util.List;

@Data
public class UserStateDTO {
    public String username;
    public UserRole role;
    public Integer cartSize;
}
