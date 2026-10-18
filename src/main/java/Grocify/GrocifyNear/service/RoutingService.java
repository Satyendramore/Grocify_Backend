package Grocify.GrocifyNear.service;

import Grocify.GrocifyNear.dto.RequestDto.DistanceFromShopDto;
import Grocify.GrocifyNear.model.Shop;

import java.util.List;

public interface RoutingService {
    List<DistanceFromShopDto<Double>> distanceFromShops(List<Shop> shops);
}
