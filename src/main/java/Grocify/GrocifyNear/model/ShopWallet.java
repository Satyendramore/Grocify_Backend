package Grocify.GrocifyNear.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long walletId;

    private double pendingAmount;

    private double settledAmount;

    @OneToOne
    private Shop shop;
}