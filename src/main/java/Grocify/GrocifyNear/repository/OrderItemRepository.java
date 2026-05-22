package Grocify.GrocifyNear.repository;

import Grocify.GrocifyNear.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {
}