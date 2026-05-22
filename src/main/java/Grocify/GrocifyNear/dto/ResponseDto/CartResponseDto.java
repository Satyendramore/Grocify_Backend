package Grocify.GrocifyNear.dto.ResponseDto;

import Grocify.GrocifyNear.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDto {
    private Double totalPrice;
    private List<CartItemResponseDto> cartItems;
}
