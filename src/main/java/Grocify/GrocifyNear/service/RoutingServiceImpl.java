package Grocify.GrocifyNear.service;

import Grocify.GrocifyNear.dto.RequestDto.DistanceFromShopDto;

import Grocify.GrocifyNear.dto.RouteInfo;
import Grocify.GrocifyNear.model.Shop;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
public class RoutingServiceImpl implements RoutingService {
    private final WebClient webClient;
    public RoutingServiceImpl( WebClient.Builder builder,
                               @Value("${graphhopper.url}") String url) {
        this.webClient = builder.baseUrl(url).build();

    }

    public List<DistanceFromShopDto<Double>> distanceFromShops(
            List<Shop> shops) {

        List<DistanceFromShopDto<Double>> distanceFromShopDtos =
                new ArrayList<>();


        for (Shop shop : shops) {

            for (Shop shopr : shops) {

                if (!shopr.getShopId().equals(
                        shop.getShopId())) {

                    RouteInfo route =
                            getRoadDistance(
                                    shop.getLatitude(),
                                    shop.getLongitude(),
                                    shopr.getLatitude(),
                                    shopr.getLongitude()
                            );


                    if (route == null) {
                        continue;
                    }


                    distanceFromShopDtos.add(
                            new DistanceFromShopDto<>(
                                    route.getDistanceKm(),
                                    route.getTimeMinutes(),
                                    shop.getShopId(),
                                    shopr.getShopId()
                            )
                    );
                }
            }
        }

        return distanceFromShopDtos;
    }


    private RouteInfo getRoadDistance(
            double lat1,
            double lon1,
            double lat2,
            double lon2) {

        try {

            Map<String, Object> response =
                    webClient.get()

                            .uri(uri -> uri
                                    .path("/route")

                                    .queryParam(
                                            "point",
                                            lat1 + "," + lon1
                                    )

                                    .queryParam(
                                            "point",
                                            lat2 + "," + lon2
                                    )

                                    .queryParam(
                                            "profile",
                                            "bike"
                                    )

                                    .queryParam(
                                            "weighting",
                                            "shortest"
                                    )

                                    .queryParam(
                                            "instructions",
                                            false
                                    )

                                    .queryParam(
                                            "calc_points",
                                            false
                                    )

                                    .build()
                            )

                            .retrieve()

                            .bodyToMono(
                                    new ParameterizedTypeReference<
                                            Map<String, Object>
                                            >() {}
                            )

                            .block();


            if (response == null) {
                return null;
            }


            List<Map<String, Object>> paths =
                    (List<Map<String, Object>>)
                            response.get("paths");


            if (paths == null || paths.isEmpty()) {
                return null;
            }


            Map<String, Object> path =
                    paths.get(0);


            double distanceMeters =
                    ((Number) path.get("distance"))
                            .doubleValue();


            double timeMilliseconds =
                    ((Number) path.get("time"))
                            .doubleValue();


            double distanceKm =
                    distanceMeters / 1000.0;


            double timeMinutes =
                    timeMilliseconds / 60000.0;


            return new RouteInfo(
                    distanceKm,
                    timeMinutes
            );


        } catch (Exception e) {

            System.out.println(
                    "Route failed: " +
                            e.getMessage()
            );

            return null;
        }
    }








        }
