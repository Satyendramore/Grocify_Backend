package Grocify.GrocifyNear.service;


import Grocify.GrocifyNear.dto.RequestDto.RiderLocationRequestDto;
import Grocify.GrocifyNear.dto.RequestDto.RiderRequestDto;
import org.springframework.security.core.Authentication;

public interface RiderService {
    String createRider(RiderRequestDto rider, Authentication authentication);

    String statusUpdate(String status, Authentication authentication);

    String addLocation(RiderLocationRequestDto location, Authentication authentication);

    String notifyRiders(Long orderId);
}
