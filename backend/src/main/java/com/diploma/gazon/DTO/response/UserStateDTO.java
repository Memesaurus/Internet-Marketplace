package com.diploma.gazon.DTO.response;

import com.diploma.gazon.models.Item;
import lombok.Data;

import java.util.List;

@Data
public class UserStateDTO {
    public String username;
    public List<Item> cart;
}
