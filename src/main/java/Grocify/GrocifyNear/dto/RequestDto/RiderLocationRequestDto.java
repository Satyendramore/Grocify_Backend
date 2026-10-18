package Grocify.GrocifyNear.dto.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiderLocationRequestDto {

    private double latitude;
    private double longitude;
}