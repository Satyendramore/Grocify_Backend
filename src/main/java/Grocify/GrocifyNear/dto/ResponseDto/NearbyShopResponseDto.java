package Grocify.GrocifyNear.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NearbyShopResponseDto {
    ShopResponseDto shop;
    Double distance;
}
