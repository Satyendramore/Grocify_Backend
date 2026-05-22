package Grocify.GrocifyNear.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;
    private String shopName;
    private Double latitude;
    private Double longitude;
    private String address;


    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    List<Product> products;


}
