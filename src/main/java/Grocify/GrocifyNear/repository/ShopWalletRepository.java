package Grocify.GrocifyNear.repository;

import Grocify.GrocifyNear.model.Shop;
import Grocify.GrocifyNear.model.ShopWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopWalletRepository
        extends JpaRepository<ShopWallet, Long> {

    Optional<ShopWallet> findByShop(Shop shop);
}