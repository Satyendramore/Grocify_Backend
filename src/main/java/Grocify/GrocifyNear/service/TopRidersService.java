package Grocify.GrocifyNear.service;

import Grocify.GrocifyNear.dto.RequestDto.RankDto;
import Grocify.GrocifyNear.dto.RequestDto.RiderDistanceDto;
import Grocify.GrocifyNear.model.Order;

import java.util.HashMap;
import java.util.Map;

public interface TopRidersService {
    HashMap<String, RiderDistanceDto> nearestRider(Order order);
    Map<Long, RankDto> topRidersWithShortestPath(Order order);
}
