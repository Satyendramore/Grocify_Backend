package Grocify.GrocifyNear.service;

import Grocify.GrocifyNear.dto.RequestDto.CartRequestDto;
import Grocify.GrocifyNear.dto.ResponseDto.CartResponseDto;
import Grocify.GrocifyNear.handler.Failed;
import Grocify.GrocifyNear.handler.ResourceNotAvailable;
import Grocify.GrocifyNear.mapper.CartMapper;
import Grocify.GrocifyNear.model.Cart;
import Grocify.GrocifyNear.model.CartItem;
import Grocify.GrocifyNear.model.Product;
import Grocify.GrocifyNear.repository.CartItemRepo;
import Grocify.GrocifyNear.repository.CartRepo;
import Grocify.GrocifyNear.repository.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class CartServiceImpl implements CartService {

    private  final CartMapper cartMapper;
    private final CartRepo cartRepo;
    private final ProductRepo productRepo;
    private final CartItemRepo cartItemRepo;


    CartServiceImpl(CartRepo cartRepo, CartMapper cartMapper, ProductRepo productRepo, CartItemRepo cartItemRepo) {
    this.cartRepo = cartRepo;
    this.cartMapper = cartMapper;
    this.productRepo = productRepo;
        this.cartItemRepo = cartItemRepo;
    }
    public CartResponseDto getCart(Authentication authentication) {
         String email = authentication.getName();
        Cart cart=cartRepo.findCartByUserEmail(email).orElseThrow(()->new ResourceNotAvailable("user not found on this id"));
        CartResponseDto cartResponseDto=cartMapper.cartResponseDtoFromCart(cart);
        return cartResponseDto;
    }

    @Override
    @Transactional
    public String addInCart(CartRequestDto cartRequestDto, Long productId, Authentication authentication) {
      try {String email=authentication.getName();
          Cart cart = cartMapper.cartFromCartItemResponseDto(
                  cartRepo.findCartByUserEmail(email).orElseThrow(() -> new ResourceNotAvailable("user not found on this id"))
                  ,cartRequestDto,productId);

          return "Item is  added successfully";
      }

      catch (Exception e) {
          throw  new Failed("addInCart failed due to the exception: "+e.getMessage() );
      }


    }

    @Transactional
    @Override
    public String deleteProductById(Long cartItemId) {
    try {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Cart cart = cartRepo.findCartByUserEmail(email).orElseThrow(() -> new ResourceNotAvailable("user not found on this id"));
        CartItem cartItem=cartItemRepo.findByCartItemId(cartItemId);
        if(cartItem==null){
            throw new ResourceNotAvailable("item not found on this id");
        }
        cart.getCartItems().remove(cartItem);
        return "Item is deleted successfully";
    }

    catch (Exception e) {
        throw  new Failed("deleteProductById failed due to the exception: "+e.getMessage() );
    }
    }


    @Transactional
    @Override
    public String deleteAllCartItems() {
      try {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          String email = authentication.getName();

          Cart cart = cartRepo.findCartByUserEmail(email).orElseThrow(() -> new ResourceNotAvailable("cart not found on this id"));
          cart.getCartItems().clear();
          return "CartItems is deleted successfully";
      }
      catch (Exception e) {
          throw  new Failed("deleteAllCartItems failed due to the exception: "+e.getMessage() );
      }
    }

}
