package Grocify.GrocifyNear.mapper;

import Grocify.GrocifyNear.dto.RequestDto.ShopRequestDto;
import Grocify.GrocifyNear.dto.ResponseDto.ShopResponseDto;
import Grocify.GrocifyNear.model.Shop;
import org.springframework.stereotype.Component;

@Component
public class ShopMapper {
    public Shop shopFromShopRequest(Shop shop,ShopRequestDto shopRequestDto) {
        shop.setShopName(shopRequestDto.getShopName());
        shop.setLatitude(shopRequestDto.getLatitude());
        shop.setLongitude(shopRequestDto.getLongitude());
        shop.setAddress(shopRequestDto.getAddress());
        return shop;
    }

    public ShopResponseDto shopResponseDtoFromShop(Shop shop) {
        ShopResponseDto shopResponseDto = new ShopResponseDto();
        shopResponseDto.setShopName(shop.getShopName());
        shopResponseDto.setShopId(shop.getShopId());
        shopResponseDto.setAddress(shop.getAddress());
        return shopResponseDto;
    }
}
