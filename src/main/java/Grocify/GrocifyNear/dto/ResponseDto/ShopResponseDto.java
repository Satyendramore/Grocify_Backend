package Grocify.GrocifyNear.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopResponseDto {
    private Long shopId;
    private String shopName;
    private String address;
}
