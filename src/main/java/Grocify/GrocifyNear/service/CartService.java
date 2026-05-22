package Grocify.GrocifyNear.service;

import Grocify.GrocifyNear.dto.RequestDto.CartRequestDto;
import Grocify.GrocifyNear.dto.ResponseDto.CartResponseDto;

public interface CartService {
    CartResponseDto getCart(Long userId);

    String addInCart(CartRequestDto cartRequestDto,Long productId,Long userId);
}
