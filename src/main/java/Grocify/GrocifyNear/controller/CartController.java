package Grocify.GrocifyNear.controller;

import Grocify.GrocifyNear.dto.RequestDto.CartRequestDto;
import Grocify.GrocifyNear.dto.ResponseDto.CartResponseDto;
import Grocify.GrocifyNear.model.CartItem;
import Grocify.GrocifyNear.repository.CartItemRepo;
import Grocify.GrocifyNear.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;
    CartController(CartService cartService ) {
        this.cartService =cartService;
    }
    @GetMapping()
    public ResponseEntity<?> cart(Authentication authentication) {
        return new ResponseEntity<>(new ApiResponse<>("here is the cart data",true,cartService.getCart(authentication)), HttpStatus.OK);
    }

    @PostMapping("/{productd}")
    public  ResponseEntity<?> addInCart(@PathVariable Long productd, @Valid @RequestBody CartRequestDto cartRequestDto, Authentication authentication) {
        return new ResponseEntity<>(new ApiResponse<>(null,true,cartService.addInCart(cartRequestDto,productd,authentication)),HttpStatus.OK);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<?> removeItemById(@PathVariable Long cartItemId) {
        return new ResponseEntity<>(new ApiResponse<>(null,true,cartService.deleteProductById(cartItemId)),HttpStatus.OK);
    }

    @DeleteMapping("/AllItems")
    public ResponseEntity<?> removeAllItems() {
        return new ResponseEntity<>(new ApiResponse<>(null,true,cartService.deleteAllCartItems()), HttpStatus.ACCEPTED);
    }



}
