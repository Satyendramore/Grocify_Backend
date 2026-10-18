package Grocify.GrocifyNear.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDto {
    private long quantity;
    private double subtotal;
    private String productName;
    private Long cartItemId;
    private Long cost;
}
