package Grocify.GrocifyNear.mapper;

import Grocify.GrocifyNear.dto.RequestDto.UserRequestDto;
import Grocify.GrocifyNear.dto.RequestDto.UserUpdateRequestDto;
import Grocify.GrocifyNear.dto.ResponseDto.NearbyShopResponseDto;
import Grocify.GrocifyNear.dto.ResponseDto.ShopResponseDto;
import Grocify.GrocifyNear.dto.ResponseDto.UserResponseDto;
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
    public User  userFromUserUpdateRequestDto(User newUser , UserUpdateRequestDto user) {

        newUser.setEmail(user.getEmail());
        newUser.setUserName(user.getUserName());
       newUser.setAge(user.getAge());
       newUser.setGender(user.getGender());
       newUser.setPhoneNo(user.getPhoneNo());

        return newUser;
    }
    public User  userFromUserRequestDto(User newUser , UserRequestDto user) {

        newUser.setEmail(user.getEmail());
        newUser.setUserName(user.getUserName());
       newUser.setPassword(user.getPassword());

        return newUser;
    }
    public UserResponseDto userResponseDtofromuser(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setAge(user.getAge());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setGender(user.getGender());
        userResponseDto.setUserName(user.getUserName());
        userResponseDto.setPhoneNo(user.getPhoneNo());

        return userResponseDto;
    }
}
