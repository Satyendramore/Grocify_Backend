package Grocify.GrocifyNear.mapper;

import Grocify.GrocifyNear.dto.RequestDto.UserRequestDto;
import Grocify.GrocifyNear.dto.ResponseDto.NearbyShopResponseDto;
import Grocify.GrocifyNear.dto.ResponseDto.ShopResponseDto;
import Grocify.GrocifyNear.model.Cart;
import Grocify.GrocifyNear.model.Shop;
import Grocify.GrocifyNear.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserMapper {
    private final ShopMapper shopMapper;
    public UserMapper(ShopMapper shopMapper) {
        this.shopMapper = shopMapper;
    }
    public NearbyShopResponseDto nearbyShopResponseDtofromshop(Shop shop,Double distance) {
        NearbyShopResponseDto shopResponseDto = new NearbyShopResponseDto();
        shopResponseDto.setShop(shopMapper.shopResponseDtoFromShop(shop));
        shopResponseDto.setDistance(distance);
        return shopResponseDto;
    }
    public User  userFromUserRequestDto(User newUser ,UserRequestDto user) {

        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setUserName(user.getUserName());
        newUser.setRole(user.getRole());

        return newUser;
    }
}
