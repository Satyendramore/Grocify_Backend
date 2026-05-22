package Grocify.GrocifyNear.mapper;

import Grocify.GrocifyNear.dto.RequestDto.ProductRequestDto;
import Grocify.GrocifyNear.dto.ResponseDto.ProductResponseDto;
import Grocify.GrocifyNear.model.Product;
import Grocify.GrocifyNear.model.Shop;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    private final ShopMapper shopMapper;
    ProductMapper(ShopMapper shopMapper) {
        this.shopMapper = shopMapper;
    }
    public Product productFromProductRequestDto(Product product,ProductRequestDto productRequestDto) {
        product.setProductName(productRequestDto.getProductName());
        product.setCost(productRequestDto.getCost());

        return product;
    }
    public ProductResponseDto productResponseDtoFromProduct(Product product) {
        ProductResponseDto productResponseDto=new ProductResponseDto();
        productResponseDto.setProductName(product.getProductName());
        productResponseDto.setProductId(product.getProductId());
        productResponseDto.setCost(product.getCost());
        productResponseDto.setShop(shopMapper.shopResponseDtoFromShop(product.getShop()));
        return productResponseDto;
    }
}
