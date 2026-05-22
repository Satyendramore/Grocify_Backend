package Grocify.GrocifyNear.controller;


import Grocify.GrocifyNear.dto.RequestDto.ShopRequestDto;
import Grocify.GrocifyNear.service.ShopService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shop")
public class ShopController {
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping
    public ResponseEntity<?> shop(){
        return new ResponseEntity<>(
                new ApiResponse<>("Shoplist fetched successfully",true,shopService.getShops())
                , HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addShop(@Valid @RequestBody ShopRequestDto shop){
       return  new ResponseEntity<>(
               new ApiResponse<>(null,true,shopService.addShop(shop))
               ,HttpStatus.ACCEPTED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateShop(@Valid @RequestBody ShopRequestDto shop, @PathVariable Long id){
        return  new ResponseEntity<>(
                new ApiResponse<>(null,true,shopService.updateShop(shop,id))
                ,HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShop(@PathVariable long id){
        return  new ResponseEntity<>( new ApiResponse<>(null ,true,shopService.deleteShop(id))
        ,HttpStatus.OK);
    }

}
