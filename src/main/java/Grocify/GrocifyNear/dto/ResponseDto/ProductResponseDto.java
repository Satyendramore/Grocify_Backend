package Grocify.GrocifyNear.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    private Long productId;
    private String productName;
    private Long cost;
    private ShopResponseDto shop;

}
