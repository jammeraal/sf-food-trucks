package com.jammeraal.sffoodtruck.trucks;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jammeraal.sffoodtruck.exceptions.DataAlreadyLoadedException;
import com.jammeraal.sffoodtruck.exceptions.TruckNotFoundException;

import lombok.RequiredArgsConstructor;

/**
 * The business logic goes here. It is pretty sparse but it is a small app that
 * doesn't do much currently. This is more about the best practices. Also, it
 * makes unit testing easier as mocking out the various classes is easier with a
 * degree of separation
 */
@Service
@RequiredArgsConstructor
public class FoodTruckService {
	// private static final Logger log =
	// LoggerFactory.getLogger(FoodTruckService.class);

	final private FoodTruckRepository repository;
	final LoadFoodTrucks foodTruckLoader;

	public List<FoodTruck> getAllFoodTrucks() {
		return repository.findAll();
	}

	public void loadFoodTrucks() {
		if (repository.findAll().isEmpty()) {
			foodTruckLoader.updateDatabase();
		} else {
			throw new DataAlreadyLoadedException();
		}
	}

	public FoodTruck getFoodTruckById(long id) {
		return repository.findById(id)
				.orElseThrow(() -> new TruckNotFoundException("Unable to find truck with id: " + id));
	}

	List<FoodTruck> getFoodTrucksByName(String name) {
		return repository.findByNameContainingIgnoreCase(name);
	}

	List<FoodTruck> getFoodTrucksByFood(String item) {
		return repository.findByFoodItemsContaining(item.toLowerCase());
	}
}
