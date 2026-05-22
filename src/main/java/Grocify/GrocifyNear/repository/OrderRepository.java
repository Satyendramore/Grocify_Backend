package Grocify.GrocifyNear.repository;

import Grocify.GrocifyNear.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository
        extends JpaRepository<Order, Long> {
}