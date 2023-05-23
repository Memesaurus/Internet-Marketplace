package com.diploma.gazon.mappers;

import com.diploma.gazon.DTO.request.CartItemDTO;
import com.diploma.gazon.models.CartItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface CartMapper {
    List<CartItemDTO> toCartItemDTOList(List<CartItem> cartItems);
}
