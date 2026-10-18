package Grocify.GrocifyNear.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteInfo {

    private double distanceKm;
    private double timeMinutes;


}