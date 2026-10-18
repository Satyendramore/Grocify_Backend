package Grocify.GrocifyNear.service;

import Grocify.GrocifyNear.dto.RequestDto.ProductRequestDto;
import Grocify.GrocifyNear.model.Product;
import Grocify.GrocifyNear.model.Shop;

import java.util.List;
import java.util.Locale;

public interface ProductService {
    List<Product> getProducts();

    String addProduct(Long shopId, ProductRequestDto product);

    String updateProduct(Long id, ProductRequestDto productRequestDto);

    String deleteProduct(Long id);

    List<Product> getProductsByShopId(Long shopId);
}
