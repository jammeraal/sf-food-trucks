package com.jammeraal.sffoodtruck.trucks;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface FoodTruckRepository extends JpaRepository<FoodTruck, Long> {
	List<FoodTruck> findByNameContainingIgnoreCase(String name);

	Optional<FoodTruck> findByNameIgnoreCase(String name);

	List<FoodTruck> findByFoodItemsContaining(String foodItems);
}
