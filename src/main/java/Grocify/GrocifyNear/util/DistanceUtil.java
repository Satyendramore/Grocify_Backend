package Grocify.GrocifyNear.util;

import Grocify.GrocifyNear.constants.RadiusConstant;
import org.springframework.stereotype.Component;

@Component
public class DistanceUtil {

    public static double calculateDistance(
            double userLat,
            double userLon,
            double shopLat,
            double shopLon) {

        double latDistance =
                Math.toRadians(shopLat - userLat);

        double lonDistance =
                Math.toRadians(shopLon - userLon);

        double a =
                Math.sin(latDistance / 2)
                * Math.sin(latDistance / 2)

                + Math.cos(Math.toRadians(userLat))
                * Math.cos(Math.toRadians(shopLat))

                * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);

        double c =
                2 * Math.atan2(
                        Math.sqrt(a),
                        Math.sqrt(1 - a)
                );

        return  RadiusConstant.EARTH_RADIUS* c;
    }
}