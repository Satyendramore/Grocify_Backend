package Grocify.GrocifyNear.repository;

import Grocify.GrocifyNear.model.Product;
import Grocify.GrocifyNear.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {
    List<Product> findByProductName(String item);

    List<Product> findByShop(Shop shop);
}
