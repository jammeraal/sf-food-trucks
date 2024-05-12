package com.jammeraal.sffoodtruck.trucks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FoodTruckTest {

	@Test
	public void toStringTest() {
		FoodTruck ft = FoodTruck.builder().facilityType("type").foodItems("food").name("bus name")
				.location(new FoodTruckLocation()).build();
		assertEquals("FoodTruck [id=0, name=bus name, facilityType=type, foodItems=food]", ft.toString());

	}
}