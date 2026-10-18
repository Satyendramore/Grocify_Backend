package Grocify.GrocifyNear.mapper;

import Grocify.GrocifyNear.dto.RequestDto.CartRequestDto;
import Grocify.GrocifyNear.dto.ResponseDto.CartItemResponseDto;
import Grocify.GrocifyNear.dto.ResponseDto.CartResponseDto;
import Grocify.GrocifyNear.handler.ResourceNotAvailable;
import Grocify.GrocifyNear.model.Cart;
import Grocify.GrocifyNear.model.CartItem;
import Grocify.GrocifyNear.repository.ProductRepo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartMapper {

    private final ProductRepo productRepo;
    CartMapper(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }
    public CartResponseDto cartResponseDtoFromCart(Cart cart) {
        CartResponseDto cartResponseDto=new CartResponseDto();
        cartResponseDto.setTotalPrice(cart.getTotalPrice());
        List<CartItemResponseDto> l=new ArrayList<>();
        for(CartItem cartItem:cart.getCartItems()){
            CartItemResponseDto cartItemResponseDto=cartItemResponseDtoFromCartItem(cartItem);
            l.add(cartItemResponseDto);
        }
        cartResponseDto.setCartItems(l);
        return cartResponseDto;
    }
    public CartItemResponseDto cartItemResponseDtoFromCartItem(CartItem cartItem) {
        CartItemResponseDto cartItemResponseDto=new CartItemResponseDto();
        cartItemResponseDto.setSubtotal(cartItem.getSubtotal());
        cartItemResponseDto.setCartItemId(cartItem.getCartItemId());
        cartItemResponseDto.setQuantity(cartItem.getQuantity());
        cartItemResponseDto.setProductName(cartItem.getProduct().getProductName());
        cartItemResponseDto.setCost(cartItem.getProduct().getCost());
        return cartItemResponseDto;
    }

    public  Cart cartFromCartItemResponseDto(Cart cart, CartRequestDto cartRequestDto, Long productId) {
        List<CartItem> cardItems = cart.getCartItems();
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(cartRequestDto.getQuantity());
        cartItem.setSubtotal(cartRequestDto.getPrice());
        cartItem.setCart(cart);
        cartItem.setProduct(productRepo.findById(productId).orElseThrow(() -> new ResourceNotAvailable("product id is wrong")));
        cardItems.add(cartItem);
        cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getSubtotal()*cartItem.getQuantity()));
        return cart;
    }

}
