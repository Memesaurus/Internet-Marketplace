package com.diploma.gazon.DTO.response;

import com.diploma.gazon.models.Item;
import com.diploma.gazon.models.User.UserRole;
import lombok.Data;

import java.util.List;

@Data
public class UserStateDTO {
    public String username;
    public UserRole role;
    public List<Item> cart;
}
