package Grocify.GrocifyNear.model;

import Grocify.GrocifyNear.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.apache.bcel.classfile.Module;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rider {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long riderId;
    private Status status;
    @Column(nullable = false)
    private String vehicleNo;

    private int rating;
    @OneToOne
    private User user;
}
