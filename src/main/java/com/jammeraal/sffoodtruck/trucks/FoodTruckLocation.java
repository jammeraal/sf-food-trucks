package com.jammeraal.sffoodtruck.trucks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a location of a food truck. As a business can have
 * multiple food trucks, this represents the location of each of the companies
 * food trucks. This class is used for 2 purposes. <br/>
 * 1. deserializing the json from the SF data site<br/>
 * 2. the database interaction.<br/>
 * <br/>
 * In a a larger project, I would likely split these 2 functionalities into
 * separate classes. This servers 2 purposes.<br/>
 * 1. It avoided confusion. one class creates a POJO from json, while another
 * class represents the database object<br/>
 * 2. Maintenance. The json will inevitably change at some point. If they are
 * together as they are
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class FoodTruckLocation {
	// id columns having more than just "id" as the name can prevent confusion in
	// complex queries
	@Column(name = "location_id")
	private @Id @GeneratedValue long id;

	private String address;
	private String locationDescription;

	/*
	 * TODO make this an enum - it helps with data sanitizing and ensuring queries
	 * don't have typos
	 */
	// REQUESTED, SUSPEND, EXPIRED, ISSUED, APPROVED,
	private String status;

	@JsonIgnoreProperties("locations") // avoid cyclic references
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "truck_id")
	private FoodTruck foodTruck;

	@Override
	public String toString() {
		return "FoodTruckLocation [id=" + id + ", address=" + address + ", locationDescription=" + locationDescription
				+ ", status=" + status + "]";
	}
}
