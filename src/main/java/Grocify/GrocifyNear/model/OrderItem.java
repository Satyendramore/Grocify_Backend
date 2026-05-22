package Grocify.GrocifyNear.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderItemId;

    private long quantity;

    private double subtotal;

    private double priceAtPurchase;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Shop shop;

    @ManyToOne
    @JsonIgnore
    private Order order;
}