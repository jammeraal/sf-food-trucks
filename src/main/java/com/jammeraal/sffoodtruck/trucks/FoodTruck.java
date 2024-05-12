package com.jammeraal.sffoodtruck.trucks;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a business, which is a grouping of food truck
 * locations. So the food truck name is essentially the business. Than each
 * location represents where you can find a truck of this type.<br/>
 * The class is used for 2 purposes. <br/>
 * 1. deserializing the json from the SF data site<br/>
 * 2. the database interaction.<br/>
 * <br/>
 * In a a larger project, I would likely split these 2 functionalities into
 * separate classes and then have a helper that can convert the json object to
 * the database object. This servers 2 purposes.<br/>
 * 1. It avoided confusion. one class creates a POJO from json, while another
 * class represents the database object<br/>
 * 2. Maintenance. The json will inevitably change at some point. If they are
 * together as they are
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@JsonDeserialize(using = FoodTruckJSONDeserializer.class)
public class FoodTruck {
	// id columns having more than just "id" as the name can prevent confusion in
	// complex queries
	@Column(name = "truck_id")
	private @Id @GeneratedValue long id;
	@Column(unique = true)
	private String name;
	private String facilityType;
	@Lob
	@Column(length = 512) // should fit everything
	private String foodItems;

	@JsonIgnoreProperties("foodTruck") // prevent infinite loops with FoodTruckLocation
	@OneToMany(mappedBy = "foodTruck", fetch = FetchType.EAGER)
	@Setter(AccessLevel.NONE) // ensure the addLocation is called rather than the setter
	private List<FoodTruckLocation> locations = new ArrayList<FoodTruckLocation>();

	@Builder
	public FoodTruck(String name, String facilityType, String foodItems, FoodTruckLocation location) {
		this.setName(name);
		this.setFacilityType(facilityType);
		this.setFoodItems(foodItems == null ? null : foodItems.toLowerCase());

		this.addLocation(location);
	}

	public void addLocation(FoodTruckLocation ftl) {
		ftl.setFoodTruck(this);
		this.locations.add(ftl);
	}

	@Override
	public String toString() {
		return "FoodTruck [id=" + id
				+ ", name=" + name
				+ ", facilityType=" + facilityType
				+ ", foodItems=" + foodItems + "]";
	}
}
