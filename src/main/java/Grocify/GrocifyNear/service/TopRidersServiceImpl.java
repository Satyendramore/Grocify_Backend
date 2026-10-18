package Grocify.GrocifyNear.service;


import Grocify.GrocifyNear.dto.RequestDto.DistanceFromShopDto;
import Grocify.GrocifyNear.dto.RequestDto.RankDto;
import Grocify.GrocifyNear.dto.RequestDto.RiderDistanceDto;
import Grocify.GrocifyNear.model.Order;
import Grocify.GrocifyNear.model.OrderItem;
import Grocify.GrocifyNear.model.Shop;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TopRidersServiceImpl  implements TopRidersService {

   private  RoutingService routingService;
    private RedisTemplate redisTemplate;





    public TopRidersServiceImpl(
       RoutingService routingService,RedisTemplate redisTemplate) {


        this.routingService = routingService;
        this.redisTemplate = redisTemplate;

    }


    public HashMap<String, RiderDistanceDto> nearestRider(Order order) {

        HashMap<String, RiderDistanceDto> riderDistances = new HashMap<>();

        for (OrderItem orderItem : order.getOrderItems()) {

            Shop shop = orderItem.getShop();

            double longitude = shop.getLongitude();
            double latitude = shop.getLatitude();

            Circle circle = new Circle(
                    new Point(longitude, latitude),
                    new Distance(5, Metrics.KILOMETERS)
            );

            GeoResults<RedisGeoCommands.GeoLocation<Object>> results =
                    redisTemplate.opsForGeo().radius(
                            "riders:available",
                            circle,
                            RedisGeoCommands.GeoRadiusCommandArgs
                                    .newGeoRadiusArgs()
                                    .includeDistance()
                                    .sortAscending()
                                    .limit(10)
                    );

            if (results != null) {

                for (GeoResult<RedisGeoCommands.GeoLocation<Object>> result : results) {

                    String riderId =
                            String.valueOf(result.getContent().getName());

                    double distance =
                            result.getDistance().getValue();

                    Long shopId = shop.getShopId();

                    RiderDistanceDto newValue =
                            new RiderDistanceDto(distance, shopId);

                    riderDistances.merge(
                            riderId,
                            newValue,
                            (oldValue, currentValue) -> {

                                if (currentValue.getDistance()
                                        < oldValue.getDistance()) {

                                    return currentValue;
                                }

                                return oldValue;
                            }
                    );
                }
            }
        }

        return riderDistances;
    }

    public Map<Long, RankDto>  topRidersWithShortestPath(Order order) {

        HashMap<String, RiderDistanceDto> riderDistance =
                new HashMap<>();



        for (int i = 0; i < 5; i++) {

            try {

                riderDistance =
                        nearestRider(order);

                if (riderDistance.size() > 0) {
                    break;
                }

            } catch (Exception e) {

                throw new RuntimeException(
                        e.getMessage()
                );
            }
        }


        if (riderDistance.size() == 0) {

            throw new RuntimeException(
                    "No rider found"
            );
        }


        // Get shops from order
        List<Shop> shops = new ArrayList<>();

        for (OrderItem orderItem : order.getOrderItems()) {

            Shop shop = orderItem.getShop();

            if (!shops.contains(shop)) {
                shops.add(shop);
            }
        }


        // Calculate shop-to-shop distances
        List<DistanceFromShopDto<Double>> distanceFromShopDtos;

        try {
            distanceFromShopDtos =
                   routingService.distanceFromShops(shops);

        } catch (Exception e) {

            throw new RuntimeException(
                    e.getMessage()
            );
        }



        Map<Long, RankDto> top10riders= top10(riderDistance, distanceFromShopDtos,shops);
        return top10riders;

    }









    public Map<Long, RankDto>  top10(
            HashMap<String, RiderDistanceDto> riderDistance,
            List<DistanceFromShopDto<Double>> distanceFromShopDtos,
            List<Shop> shops) {

        HashMap<String, DistanceFromShopDto<Double>> hm = new HashMap<>();

        for (DistanceFromShopDto<Double> distanceFromShopDto : distanceFromShopDtos) {

            String key1 = distanceFromShopDto.getShopA() + "." + distanceFromShopDto.getShopB();
            String key2 = distanceFromShopDto.getShopB() + "." + distanceFromShopDto.getShopA();

            hm.put(key1, distanceFromShopDto);
            hm.put(key2, distanceFromShopDto);
        }

        HashMap<Long, RankDto> rank = new HashMap<>();

        for (Map.Entry<String, RiderDistanceDto> entry : riderDistance.entrySet()) {

            long riderId = Long.parseLong(entry.getKey());

            List<Long> shopIds = new ArrayList<>();

            for (Shop shop : shops) {

                if (!entry.getValue().getShopId().equals(shop.getShopId())) {
                    shopIds.add(shop.getShopId());
                }
            }

            RankDto rankDto1 = perm(
                    hm,
                    entry.getValue().getShopId().toString(),
                    entry.getValue().getDistance(),
                    shopIds,
                    entry.getValue().getShopId()
            );

            rank.put(riderId, rankDto1);
        }

        // Sort riders according to lowest rank
        List<Map.Entry<Long, RankDto>> sorted =
                new ArrayList<>(rank.entrySet());

        sorted.sort(
                Comparator.comparingDouble(
                        entry -> entry.getValue().getRank1()
                )
        );

        Map<Long, RankDto> sortedMap = new LinkedHashMap<>();

        int count = 0;

        for (Map.Entry<Long, RankDto> entry : sorted) {

            sortedMap.put(entry.getKey(),entry.getValue());

            System.out.println(
                    "Rider: " + entry.getKey()
                            + " Rank: " + entry.getValue().getRank1()
                            + " Path: " + entry.getValue().getPath()
            );

            count++;

            if (count == 10) {
                break;
            }
        }

        return sortedMap;
    }


    public RankDto perm(
            HashMap<String, DistanceFromShopDto<Double>> hm,
            String path,
            double totalRank,
            List<Long> shopIds,
            Long prevShopId) {

        // All shops visited
        if (shopIds.size() == 0) {
            return new RankDto(totalRank, path);
        }

        RankDto bestRankDto = null;

        for (int i = 0; i < shopIds.size(); i++) {

            Long shopId = shopIds.get(i);

            String key = prevShopId + "." + shopId;

            DistanceFromShopDto<Double> distanceFromShopDto = hm.get(key);

            if (distanceFromShopDto == null) {
                continue;
            }

            // Remove current shop
            shopIds.remove(i);

            RankDto currentRankDto = perm(
                    hm,
                    path + " -> " + shopId,
                    totalRank
                            + distanceFromShopDto.getDistance()
                            + distanceFromShopDto.getTimeMinutes(),
                    shopIds,
                    shopId
            );

            // Put it back
            shopIds.add(i, shopId);

            // Keep minimum
            if (bestRankDto == null ||
                    currentRankDto.getRank1() < bestRankDto.getRank1()) {

                bestRankDto = currentRankDto;
            }
        }

        return bestRankDto;
    }


}