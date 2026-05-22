package Grocify.GrocifyNear.controller;

import Grocify.GrocifyNear.dto.RequestDto.CartRequestDto;
import Grocify.GrocifyNear.dto.ResponseDto.CartResponseDto;
import Grocify.GrocifyNear.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    private final CartService cartService;
    CartController(CartService cartService ) {
        this.cartService =cartService;
    }
    @GetMapping("/{userId}")
    public ResponseEntity<?> cart(@PathVariable Long userId) {
        return new ResponseEntity<>(new ApiResponse<>("here is the cart data",true,cartService.getCart(userId)), HttpStatus.OK);
    }

    @PostMapping("/{productd}")
    public  ResponseEntity<?> addInCart(@PathVariable Long productd, @Valid @RequestBody CartRequestDto cartRequestDto, @RequestParam long userId) {
        return new ResponseEntity<>(new ApiResponse<>(null,true,cartService.addInCart(cartRequestDto,productd,userId)),HttpStatus.OK);
    }

}
