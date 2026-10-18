package Grocify.GrocifyNear.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiderResponseDto {

    private Long orderId;
    private boolean accepted;

}