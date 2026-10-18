package Grocify.GrocifyNear.controller;

import Grocify.GrocifyNear.dto.RequestDto.LoginRequestDto;
import Grocify.GrocifyNear.dto.RequestDto.UserRequestDto;
import Grocify.GrocifyNear.dto.RequestDto.UserUpdateRequestDto;
import Grocify.GrocifyNear.service.UserService;
import jakarta.validation.Valid;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final ErrorController errorController;

    public UserController(UserService userService, ErrorController errorController) {
        this.userService = userService;
        this.errorController = errorController;
    }

    @GetMapping("/shop/nearby")
    public ResponseEntity<?> shopNearby(@RequestParam double lat, @RequestParam double lng, @RequestParam double radius) {
        return new ResponseEntity<>(new ApiResponse<>("Nearby shop list according to range provided by user", true, userService.getShopsByRadius(lat, lng, radius)), HttpStatus.OK);
    }

    @GetMapping("/products/compare")
    public ResponseEntity<?> compare(@RequestParam String item) {
        return new ResponseEntity<>(new ApiResponse<>("items currently available", true, userService.getProductsByName(item)), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDto user) {
        return new ResponseEntity<>(new ApiResponse<>(null, true, userService.addUser(user)), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto dto) {
        return new ResponseEntity<>(new ApiResponse<>("Login successful", true, userService.login(dto)), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateRequestDto user) {
        return new ResponseEntity<>(new ApiResponse<>(null, true, userService.updateUser(user)), HttpStatus.OK);
    }


    @GetMapping()
    public ResponseEntity<?> getUser() {
        return  new ResponseEntity<>(new ApiResponse<>("User Info",true,userService.getUser()),HttpStatus.OK);
    }


}
