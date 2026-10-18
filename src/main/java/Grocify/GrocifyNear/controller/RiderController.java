package Grocify.GrocifyNear.controller;

import Grocify.GrocifyNear.dto.RequestDto.RiderLocationRequestDto;
import Grocify.GrocifyNear.dto.RequestDto.RiderRequestDto;

import Grocify.GrocifyNear.dto.ResponseDto.RiderResponseDto;
import Grocify.GrocifyNear.service.RiderNotificationService;
import Grocify.GrocifyNear.service.RiderNotificationServiceImpl;
import Grocify.GrocifyNear.service.RiderService;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rider")
public class RiderController {
   private RiderService riderService;
    private final RiderNotificationService service;
   RiderController(RiderService riderService, RiderNotificationService riderNotificationService) {
       this.riderService = riderService;
       this.service = riderNotificationService;
   }
    @PostMapping("/")
    public ResponseEntity<?> createRider(@RequestBody RiderRequestDto rider,   Authentication authentication) {
       return new ResponseEntity<>(riderService.createRider(rider,authentication), HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateRider(@RequestParam String status, Authentication authentication) {

       return  new ResponseEntity<>(riderService.statusUpdate(status,authentication),HttpStatus.OK);
    }


    @PostMapping("/location")
    public ResponseEntity<?> locationstore(@RequestBody RiderLocationRequestDto location, Authentication authentication) {
          return  new ResponseEntity<>(riderService.addLocation(location,authentication),HttpStatus.OK);
    }


    @MessageMapping("/rider/assignment-response")
    public void respond(RiderResponseDto dto,
                        HttpExchange.Principal principal) {

        service.receiveResponse(
                principal.getName(),
                dto.getOrderId(),
                dto.isAccepted()
        );
    }

    @PostMapping("/notify/{orderId}")
    public ResponseEntity<String> notifyRiders(@PathVariable Long orderId) {


        return new ResponseEntity<>(riderService.notifyRiders(orderId),HttpStatus.OK);
    }









}
