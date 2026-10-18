package Grocify.GrocifyNear.dto.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiderDistanceDto {

    private double distance;
    private Long shopId;


}