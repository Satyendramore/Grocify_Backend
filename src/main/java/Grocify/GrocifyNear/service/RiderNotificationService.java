package Grocify.GrocifyNear.service;

import Grocify.GrocifyNear.dto.RequestDto.RankDto;
import Grocify.GrocifyNear.model.Order;

public interface RiderNotificationService {
     void sendOffer(
            String riderUsername,
            Long orderId,
            RankDto rankDto);
      void receiveResponse(
             String email,
             Long orderId,
             boolean accepted);
      void notifyTopRider(Order order);
}
