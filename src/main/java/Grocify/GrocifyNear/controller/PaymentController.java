package Grocify.GrocifyNear.controller;

import Grocify.GrocifyNear.dto.RequestDto.PaymentVerifyRequestDto;
import Grocify.GrocifyNear.service.OrderService;
import Grocify.GrocifyNear.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {
private final OrderService orderService;
private final PaymentService paymentService;
public PaymentController(OrderService orderService, PaymentService paymentService) {
    this.orderService = orderService;
    this.paymentService = paymentService;

}
    @PostMapping("/create/order")
    public ResponseEntity<?> createOrder(@RequestParam Long userId) {
        return new ResponseEntity<>(new ApiResponse<>("Here is order details",true,orderService.getOrderDetails(userId)), HttpStatus.OK);

    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@Valid @RequestBody PaymentVerifyRequestDto dto) {
        return new ResponseEntity<>(new ApiResponse<>("Successfully verified payment", true, paymentService.placeOrder(dto)), HttpStatus.OK);
    }
}