package Grocify.GrocifyNear.service;

import Grocify.GrocifyNear.dto.RequestDto.RiderDistanceDto;
import Grocify.GrocifyNear.dto.RequestDto.RiderLocationRequestDto;
import Grocify.GrocifyNear.dto.RequestDto.RiderRequestDto;
import Grocify.GrocifyNear.enums.Status;
import Grocify.GrocifyNear.model.*;
import Grocify.GrocifyNear.repository.OrderRepository;
import Grocify.GrocifyNear.repository.RiderRepo;
import Grocify.GrocifyNear.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class RiderServiceImpl implements RiderService {
    private UserRepo userRepo;
    private RiderRepo riderRepo;
    private RedisTemplate redisTemplate;
    private OrderRepository orderRepository;
    private RiderNotificationService riderNotificationService;
    public RiderServiceImpl(UserRepo userRepo, RiderRepo riderRepo, RedisTemplate redisTemplate, OrderRepository orderRepository, RiderNotificationService riderNotificationService) {
        this.userRepo = userRepo;
        this.riderRepo = riderRepo;
        this.redisTemplate = redisTemplate;
        this.orderRepository = orderRepository;
        this.riderNotificationService = riderNotificationService;
    }
    @Override
    @Transactional
    public String createRider(RiderRequestDto rider, Authentication authentication ) {
        try{
            String userId=authentication.getName();
            User user=userRepo.findByEmail(userId).orElse(null);
            if(user==null){
                throw  new RuntimeException("User not found");
            }
            Rider rider1=new Rider();
            rider1.setUser(user);
            rider1.setStatus(Status.Offline);
            rider1.setRating(0);
            rider1.setVehicleNo(rider.getVehicleNo());
            riderRepo.save(rider1);
            return "rider created successfully";
        }
        catch (Exception e){
            return "rider creation failed";
        }

    }

    @Transactional
    @Override
    public String statusUpdate(String status,Authentication authentication) {
        try {
            String userId=authentication.getName();
            User user=userRepo.findByEmail(userId).orElse(null);
            if(user==null){
                throw  new RuntimeException("User not found");
            }
            user.getRider().setStatus(Status.valueOf(status));

            if(status.equals(Status.Offline.toString())){
                redisTemplate.opsForGeo().remove(
                        "riders:available",
                        String.valueOf(user.getRider().getRiderId())
                );
            }
            return riderRepo.save(user.getRider()).toString();
        }
        catch (Exception e){
            return "rider status update failed";
        }
    }
    @Transactional
    @Override
    public String addLocation(RiderLocationRequestDto location, Authentication authentication) {
       try {
           String userId=authentication.getName();
           User user=userRepo.findByEmail(userId).orElse(null);

           if (user == null) {
               throw new RuntimeException("User not found");
           }

           Rider rider = user.getRider();

           if (rider == null) {
               throw new RuntimeException("Rider not found");
           }

           redisTemplate.opsForGeo().add(
                   "riders:available",
                   new Point(location.getLongitude(), location.getLatitude()),
                   String.valueOf(rider.getRiderId())
           );
        return "rider location added successfully";
       }
       catch (Exception e){
           return "rider add location failed";
       }
    }

    @Override
    @Transactional
    public String notifyRiders(Long orderId) {
       try {
           Order order = orderRepository.findById(orderId).orElse(null);

           if (order == null) {
               throw new RuntimeException("Order not found");
           }
           riderNotificationService.notifyTopRider(order);
           return "rider notification notify successfully";

       }
       catch (Exception e){
           return "order notify failed"+e.getMessage();
       }
    }


}
