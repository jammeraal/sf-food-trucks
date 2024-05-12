package com.jammeraal.sffoodtruck.trucks;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

/**
 * Controller for interacting with trucks.
 */
@RestController()
@RequestMapping("trucks")
@RequiredArgsConstructor
public class FoodTruckController {
	// private static final Logger log =
	// LoggerFactory.getLogger(FoodTruckController.class);

	final FoodTruckService foodTruckService;

	@Operation(summary = "Get all trucks", description = "Returns a list of all the trucks and their locations")
	@GetMapping("")
	public List<FoodTruck> getAllTrucks() {
		return foodTruckService.getAllFoodTrucks();
	}

	@Operation(summary = "Load Database", description = "Go out to SF truck listing and load application database")
	@PostMapping("")
	void setupDatabase() {
		foodTruckService.loadFoodTrucks();
	}

	@Operation(summary = "Get a single truck by id", description = "Returns a truck and all of its locations by id")
	@GetMapping("/{id}")
	public FoodTruck getById(@PathVariable long id) {
		return foodTruckService.getFoodTruckById(id);
	}

	/**
	 * TODO this should really be done with query parameters in "/" getAllTrucks()
	 * 
	 * @param name - the partial name of a truck
	 * @return
	 */
	@Operation(summary = "Get trucks containing name", description = "Get all trucks with a name containing passed in text")
	@GetMapping("/name/{name}")
	List<FoodTruck> byName(@PathVariable String name) {
		return foodTruckService.getFoodTrucksByName(name);
	}

	/**
	 * TODO this should really be done with query parameters in "/" getAllTrucks()
	 * 
	 * @param item - the full/partial name of a food item
	 * @return
	 */
	@Operation(summary = "Get trucks containing food item", description = "Get all trucks with the specified food item in their list of food items")
	@GetMapping("/food/{item}")
	List<FoodTruck> byFood(@PathVariable String item) {
		return foodTruckService.getFoodTrucksByFood(item);
	}
}
