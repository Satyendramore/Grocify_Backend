package Grocify.GrocifyNear.service;

import Grocify.GrocifyNear.dto.RequestDto.CartRequestDto;
import Grocify.GrocifyNear.dto.ResponseDto.CartResponseDto;
import Grocify.GrocifyNear.handler.Failed;
import Grocify.GrocifyNear.handler.ResourceNotAvailable;
import Grocify.GrocifyNear.mapper.CartMapper;
import Grocify.GrocifyNear.model.Cart;
import Grocify.GrocifyNear.repository.CartRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service
public class CartServiceImpl implements CartService {
    private  final CartMapper cartMapper;
    private final CartRepo cartRepo;


CartServiceImpl(CartRepo cartRepo, CartMapper cartMapper) {
    this.cartRepo = cartRepo;
    this.cartMapper = cartMapper;

}
    public CartResponseDto getCart(Long userId) {
        Cart cart=cartRepo.findByUserUserId(userId).orElseThrow(()->new ResourceNotAvailable("user not found on this id"));
        CartResponseDto cartResponseDto=cartMapper.cartResponseDtoFromCart(cart);
        return cartResponseDto;
    }

    @Override
    @Transactional
    public String addInCart(CartRequestDto cartRequestDto, Long productId, Long userId) {
      try {
          Cart cart = cartMapper.cartFromCartItemResponseDto(
                  cartRepo.findByUserUserId(userId).orElseThrow(() -> new ResourceNotAvailable("user not found on this id"))
                  ,cartRequestDto,productId);

          return "Item is  added successfully";
      }

      catch (Exception e) {
          throw  new Failed("addInCart failed due to the exception: "+e.getMessage() );
      }


    }
}
