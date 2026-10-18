package Grocify.GrocifyNear.service;


import Grocify.GrocifyNear.dto.RequestDto.RankDto;
import Grocify.GrocifyNear.dto.RequestDto.RiderNotificationRequestDto;
import Grocify.GrocifyNear.model.Order;
import Grocify.GrocifyNear.model.Rider;
import Grocify.GrocifyNear.repository.RiderRepo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RiderNotificationServiceImpl implements RiderNotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final RiderRepo riderRepo;
    private final TopRidersService topRidersService;

    private final Map<String, CompletableFuture<Boolean>> pending =
            new ConcurrentHashMap<>();

    public RiderNotificationServiceImpl(
            SimpMessagingTemplate messagingTemplate,
            RiderRepo riderRepo,
            TopRidersService topRidersService) {
        this.messagingTemplate = messagingTemplate;
        this.riderRepo = riderRepo;
        this.topRidersService = topRidersService;
    }

    public void sendOffer(
            String email,
            Long orderId,
            RankDto rankDto) {

        RiderNotificationRequestDto dto =
                new RiderNotificationRequestDto(
                        orderId,
                        "New delivery order available!",
                        rankDto.getRank1(),
                        rankDto.getPath()
                );

        messagingTemplate.convertAndSendToUser(
                email,
                "/queue/assignment",
                dto
        );
    }


    private boolean waitForResponse(
            String email,
            Long orderId,
            RankDto rankDto) {

        String key = orderId + ":" + email;

        CompletableFuture<Boolean> future =
                new CompletableFuture<>();

        pending.put(key, future);

        try {
            sendOffer(email, orderId, rankDto);
            return future.get(20, TimeUnit.SECONDS);

        }  catch (Exception e) {
            Thread.currentThread().interrupt();
            return false;

        } finally {
            pending.remove(key, future);
        }
    }


    public void receiveResponse(
            String email,
            Long orderId,
            boolean accepted) {

        String key = orderId + ":" + email;

        CompletableFuture<Boolean> future = pending.get(key);

        if (future != null) {
            future.complete(accepted);
        }
    }

    public void notifyTopRider(Order order) {

        Map<Long, RankDto> hm =
                topRidersService.topRidersWithShortestPath(order);

        for (Map.Entry<Long, RankDto> entry : hm.entrySet()) {

            Rider rider =
                    riderRepo.findRiderByRiderId(entry.getKey());

            if (rider == null || rider.getUser() == null) {
                continue;
            }

            String email = rider.getUser().getEmail();

            boolean accepted = waitForResponse(
                    email,
                    order.getOrderId(),
                    entry.getValue()
            );

            if (accepted) {
                //save database info
                System.out.println("Rider accepted: " + email);
                return;
            }


            System.out.println("Trying next rider...");
        }

        System.out.println("No rider accepted the order.");
    }
}


