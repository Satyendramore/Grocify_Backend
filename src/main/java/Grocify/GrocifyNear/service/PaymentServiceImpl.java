package Grocify.GrocifyNear.service;

import Grocify.GrocifyNear.dto.RequestDto.PaymentVerifyRequestDto;
import Grocify.GrocifyNear.handler.Failed;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${razorpay.secret}")
    private String SECRET;

    private final OrderService orderService;

    public PaymentServiceImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    public String placeOrder(PaymentVerifyRequestDto dto) {
        try {
            String generatedSignature = Utils.getHash(
                    dto.getRazorpayOrderId() + "|" + dto.getRazorpayPaymentId(),
                    SECRET
            );

            if (!generatedSignature.equals(dto.getRazorpaySignature())) {
                throw new Failed("Invalid payment signature. Payment verification failed.");
            }

            orderService.placeOrder(dto.getUserId(), dto.getRazorpayPaymentId());
            return "Order placed successfully";

        } catch (RazorpayException e) {
            throw new Failed("Payment verification error: " + e.getMessage());
        }
    }
}
