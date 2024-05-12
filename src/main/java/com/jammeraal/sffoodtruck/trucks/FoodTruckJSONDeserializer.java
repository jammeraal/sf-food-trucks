package com.jammeraal.sffoodtruck.trucks;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * due to the nature of a flat json file and the many-to-one relationship
 * between food trucks and their locations, it was just simpler to implement a
 * custom deserializer
 */
public class FoodTruckJSONDeserializer extends StdDeserializer<FoodTruck> {

	private static final long serialVersionUID = 1L;

	public FoodTruckJSONDeserializer() {
		this(null);
	}

	public FoodTruckJSONDeserializer(Class<FoodTruck> t) {
		super(t);
	}

	/**
	 * Helper method to just return null as default asText method would explode on
	 * null
	 * 
	 * @param element the JsonNode element that you want the value of
	 * @return null if element is null else the value of the element as a String
	 */
	private String asText(JsonNode element) {
		return element == null ? null : element.asText();
	}

	@Override
	public FoodTruck deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);

		String name = node.get("applicant").asText();
		String facilityType = asText(node.get("facilitytype"));
		String foodItems = asText(node.get("fooditems"));

		FoodTruckLocation location = new FoodTruckLocation();
		location.setAddress(asText(node.get("address")));
		location.setLocationDescription(asText(node.get("locationdescription")));
		location.setStatus(asText(node.get("status")));

		return FoodTruck.builder().location(location).name(name).facilityType(facilityType).foodItems(foodItems)
				.build();
	}
}
