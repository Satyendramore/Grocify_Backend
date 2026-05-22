package Grocify.GrocifyNear.service;

import Grocify.GrocifyNear.dto.RequestDto.ShopRequestDto;
import Grocify.GrocifyNear.dto.ResponseDto.ShopResponseDto;
import Grocify.GrocifyNear.handler.Failed;
import Grocify.GrocifyNear.handler.ResourceNotAvailable;
import Grocify.GrocifyNear.mapper.ShopMapper;
import Grocify.GrocifyNear.model.Shop;
import Grocify.GrocifyNear.repository.ShopRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    private final ShopRepo shoprepo;
    private final ShopMapper shopMapper;
    public ShopServiceImpl(ShopRepo shoprepo, ShopMapper shopMapper) {

        this.shoprepo = shoprepo;
        this.shopMapper = shopMapper;
    }

    @Override
    public List<ShopResponseDto> getShops(){
        List<Shop> shops= shoprepo.findAll();
        List<ShopResponseDto> shopResponseDtos = new ArrayList<>();
        shops.forEach(shop -> {
            ShopResponseDto shopResponseDto = shopMapper.shopResponseDtoFromShop(shop);
            shopResponseDtos.add(shopResponseDto);

        });
        return shopResponseDtos;
    }

    @Override
    @Transactional
    public String addShop(ShopRequestDto shop) {
        try {

            Shop shopEntity = shopMapper.shopFromShopRequest(new Shop(),shop);

            shopEntity.setProducts(new ArrayList<>());

            shoprepo.save(shopEntity);

            return "Shop registered successfully";

        } catch (Exception e) {
            throw new Failed("Failed to add shop because of this exception :" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public String updateShop(ShopRequestDto shop, Long id) {

       try {
           Shop shopEntity = shopMapper.shopFromShopRequest(shoprepo.findById(id)
                   .orElseThrow(() ->
                           new ResourceNotAvailable("Shop not found")
                   ), shop);


           return "Shop data updated successfully";
       }
       catch (Exception e) {
           throw new Failed("Failed to update shop because of this exception :" + e.getMessage());
       }
    }

    @Override
    @Transactional
    public String deleteShop(Long id) {
     try{
         shoprepo.deleteById(id);
         return "shop deleted successfully";
     }
     catch (Exception e){
        throw new Failed("Failed to delete shop because of this exception :" + e.getMessage());
     }

    }


}
