package com.innocamp.dduha.repository.restaurant;

import com.innocamp.dduha.model.restaurant.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findById(Long restId);
    List<Restaurant> findByNameContaining(String keyword);
    List<Restaurant> findByRegionAndNameContaining(String region,String keyword);
    List<Restaurant> findByRegionAndNameContainingOrRegionAndNameContaining(String region1, String keyword1, String region2, String keyword2);
    List<Restaurant> findByRegionAndNameContainingOrRegionAndNameContainingOrRegionAndNameContaining(String region1, String keyword1, String region2, String keyword2, String region3, String keyword3);
    List<Restaurant> findAllByLatitudeBetweenAndLongitudeBetween(double latitude1, double latitude2, double longitude1, double longitude2);
    Page<Restaurant> findByRegion(Pageable pageable, String region);
    Page<Restaurant> findByRegionOrRegionOrRegion(Pageable pageable, String region1, String region2, String region3);
    @Query("select r from Restaurant r where exists (select rn from RestaurantNearby rn where rn.restaurant=r)")
    Page<Restaurant> findByHasStation(Pageable pageable);
    @Query("select r from Restaurant r where exists (select rn from RestaurantNearby rn where rn.restaurant=r) and r.region in (:region1, :region2, :region3)")
    Page<Restaurant> findByHasStationAndRegion(Pageable pageable, String region1, String region2, String region3);

}
