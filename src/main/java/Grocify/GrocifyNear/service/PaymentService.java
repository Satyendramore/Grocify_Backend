package Grocify.GrocifyNear.service;

import Grocify.GrocifyNear.dto.RequestDto.PaymentVerifyRequestDto;

public interface PaymentService {
    String placeOrder(PaymentVerifyRequestDto dto);
}
