package Grocify.GrocifyNear.repository;

import Grocify.GrocifyNear.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserUserId(Long userId);
    @Query("SELECT c FROM Cart c JOIN c.user u WHERE u.email = :email")
    Optional<Cart> findCartByUserEmail(@Param("email") String email);
}
