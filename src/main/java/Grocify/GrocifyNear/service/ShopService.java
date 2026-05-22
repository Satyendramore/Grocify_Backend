package Grocify.GrocifyNear.service;

import Grocify.GrocifyNear.dto.RequestDto.ShopRequestDto;
import Grocify.GrocifyNear.dto.ResponseDto.ShopResponseDto;
import Grocify.GrocifyNear.model.Shop;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShopService {
    List<ShopResponseDto> getShops();

    String addShop(ShopRequestDto shop);

    String updateShop(ShopRequestDto shop,Long id);

    String deleteShop(Long id);
}
