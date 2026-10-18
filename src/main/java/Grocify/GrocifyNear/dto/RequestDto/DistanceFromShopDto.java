package Grocify.GrocifyNear.dto.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistanceFromShopDto<T> {

    private T distance;
    private double timeMinutes;
    private Long shopA;
    private Long shopB;
}