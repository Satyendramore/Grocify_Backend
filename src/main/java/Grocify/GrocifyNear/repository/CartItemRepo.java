package Grocify.GrocifyNear.repository;

import Grocify.GrocifyNear.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Integer> {
    @Query("delete from CartItem  c where c.product.productId= :productId")
    void deleteCartItemsByProductId(Long productId);

    CartItem findByCartItemId(long cartItemId);
}
