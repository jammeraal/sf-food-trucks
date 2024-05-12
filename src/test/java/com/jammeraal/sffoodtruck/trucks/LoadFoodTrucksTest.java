package com.jammeraal.sffoodtruck.trucks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jammeraal.sffoodtruck.exceptions.UnableToLoadDataException;

@SpringBootTest
public class LoadFoodTrucksTest {
	private final String testDataFile = "src/test/java/test_data.json";
	@Autowired
	private FoodTruckRepository repository;
	@Autowired
	private FoodTruckLocationRepository locationRepo;

	@Test
	public void test1() {
		LoadFoodTrucks loader = new LoadFoodTrucks(repository, locationRepo);
		loader.updateDatabase(Path.of(testDataFile).toUri().toString());
		assertEquals(4, repository.findAll().size());
		// 2 trucks have different locations
		assertEquals(6, locationRepo.findAll().size());
	}

	@Test
	public void testFileNotFound() {
		LoadFoodTrucks loader = new LoadFoodTrucks(repository, locationRepo);
		assertThrows(UnableToLoadDataException.class, () -> {
			loader.updateDatabase(Path.of(testDataFile + "not_found").toUri().toString());
		});
	}
}
