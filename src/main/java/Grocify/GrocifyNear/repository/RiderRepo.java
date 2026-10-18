package Grocify.GrocifyNear.repository;

import Grocify.GrocifyNear.model.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderRepo extends JpaRepository<Rider, Integer> {
    Rider findRiderByRiderId(long riderId);
}
