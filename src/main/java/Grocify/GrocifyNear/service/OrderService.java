package Grocify.GrocifyNear.service;

import Grocify.GrocifyNear.dto.RequestDto.PaymentVerifyRequestDto;

public interface OrderService {

    String getOrderDetails(Long userId);


    void placeOrder(Long userId, String razorpayPaymentId);
}
