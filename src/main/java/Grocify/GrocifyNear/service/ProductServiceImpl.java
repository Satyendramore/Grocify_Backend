package Grocify.GrocifyNear.service;

import Grocify.GrocifyNear.dto.RequestDto.ProductRequestDto;
import Grocify.GrocifyNear.handler.Failed;
import Grocify.GrocifyNear.handler.ResourceNotAvailable;
import Grocify.GrocifyNear.mapper.ProductMapper;
import Grocify.GrocifyNear.model.Product;
import Grocify.GrocifyNear.model.Shop;
import Grocify.GrocifyNear.repository.ProductRepo;
import Grocify.GrocifyNear.repository.ShopRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
private final ProductRepo productRepo;
private final ShopRepo shopRepo;
private final ProductMapper productMapper;
public ProductServiceImpl(ProductRepo productRepo, ShopRepo shopRepo, ProductMapper productMapper) {
    this.productRepo = productRepo;
    this.shopRepo = shopRepo;
    this.productMapper = productMapper;
}
    @Override
    public List<Product> getProducts() {
        return productRepo.findAll();
    }

    @Override
    @Transactional
    public String addProduct(Long shopId, ProductRequestDto product) {

       try {
           Product product1 = productMapper.productFromProductRequestDto(new Product(),product);
           Shop shop = shopRepo.findById(shopId)
                   .orElseThrow(() -> new ResourceNotAvailable("Shop doesn't exist"));

           product1.setShop(shop);

           productRepo.save(product1);

           return "Product added successfully";

       }
       catch (Exception e) {
           throw new Failed("Failed to add product because of this exception :" + e.getMessage());
       }
    }

    @Override
    @Transactional
    public String updateProduct(Long id, ProductRequestDto productRequestDto) {
       try {
           Product product1 = productMapper.productFromProductRequestDto(
                   productRepo.findById(id).orElseThrow(() -> new ResourceNotAvailable("Product doesn't exist"))
                   ,productRequestDto);


           return  "product updated successfully";
       }
       catch (Exception e) {
           throw new Failed("Failed to update product because of this exception :" + e.getMessage());
       }

    }

    @Override
    @Transactional
    public String deleteProduct(Long id) {
    try {
        productRepo.deleteById(id);
        return "Product deleted successfully";
    }
    catch (Exception e) {
       throw new Failed("Failed to delete product because of this exception :" + e.getMessage());
    }

    }

    @Override
    public List<Product> getProductsByShopId(Long shopId) {
    Shop shop=shopRepo.findById(shopId).orElse(null);
    if(shop==null){
        throw new ResourceNotAvailable("Shop doesn't exist");
    }
      return productRepo.findByShop(shop);
    }


}
