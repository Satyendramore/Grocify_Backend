package Grocify.GrocifyNear.service;

import Grocify.GrocifyNear.dto.RequestDto.CartRequestDto;
import Grocify.GrocifyNear.dto.ResponseDto.CartResponseDto;
import org.springframework.security.core.Authentication;

public interface CartService {
    CartResponseDto getCart(Authentication authentication);

    String addInCart(CartRequestDto cartRequestDto, Long id, Authentication authentication);


    String deleteProductById(Long cartItemId);

    String deleteAllCartItems();
}
