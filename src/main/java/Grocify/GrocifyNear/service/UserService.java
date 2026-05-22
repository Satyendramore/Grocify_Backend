package Grocify.GrocifyNear.service;

import Grocify.GrocifyNear.dto.RequestDto.LoginRequestDto;
import Grocify.GrocifyNear.dto.RequestDto.UserRequestDto;
import Grocify.GrocifyNear.dto.ResponseDto.NearbyShopResponseDto;
import Grocify.GrocifyNear.dto.ResponseDto.ProductResponseDto;

import java.util.List;

public interface UserService {
    List<NearbyShopResponseDto> getShopsByRadius(double lat, double lng, double radius);
    List<ProductResponseDto> getProductsByName(String item);
    String addUser(UserRequestDto user);
    String updateUser(UserRequestDto user);
    String login(LoginRequestDto dto);
}
