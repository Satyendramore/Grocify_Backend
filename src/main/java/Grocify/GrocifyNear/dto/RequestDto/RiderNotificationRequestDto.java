package Grocify.GrocifyNear.dto.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiderNotificationRequestDto {

    private Long orderId;
    private String message;
    private double rank;
    private String route;


}