package com.jammeraal.sffoodtruck.trucks;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

interface FoodTruckLocationRepository extends JpaRepository<FoodTruckLocation, Long> {
	List<FoodTruckLocation> findByAddressAndLocationDescription(String address, String locationDescription);
}
