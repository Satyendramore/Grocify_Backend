package Grocify.GrocifyNear.model;

import Grocify.GrocifyNear.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;
    private String userName;
    private String password;
    private String email;
    private String age;
    private String gender;
    private String phoneNo;
    @OneToOne(mappedBy="user")
    @JsonIgnore
    private Cart cart;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL)
    private Rider rider;

}
