package com.jammeraal.sffoodtruck.trucks;

import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jammeraal.sffoodtruck.exceptions.UnableToLoadDataException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class LoadFoodTrucks {// implements CommandLineRunner {
	private static final Logger log = LoggerFactory.getLogger(LoadFoodTrucks.class);

	final private FoodTruckRepository repository;
	final private FoodTruckLocationRepository locationRepo;

	// implement commandlinerunner and uncomment this run method if you wanted to do
	// this load on startup
//	@Override
//	public void run(String... args) throws Exception {
//		updateDatabase();
//	}

	public void updateDatabase() {
		// TODO This URL should be moved into a config
		updateDatabase("https://data.sfgov.org/resource/rqzj-sfat.json");
	}

	void updateDatabase(String urlString) {
		log.info("loading data from: " + urlString);
		try {
			URL url = new URL(urlString);
			ObjectMapper objectMapper = new ObjectMapper();
			List<FoodTruck> trucks = objectMapper.readValue(url, new TypeReference<List<FoodTruck>>() {
			});

			for (FoodTruck ft : trucks) {
				FoodTruck dbFT = repository.findByNameIgnoreCase(ft.getName()).orElse(null);
				if (dbFT == null) {
					log.info("Preloading " + repository.save(ft));
					// because we are dealing with a parsed element from the data file, there will
					// only ever be one location at this point
					locationRepo.save(ft.getLocations().get(0));
				} else {
					log.debug("database version: " + dbFT);

					// we have a DB version. We need to get the location for the current ft and make
					// sure it "belongs" to the DB version

					// same as above, parsing data file means only one location in ft
					FoodTruckLocation ftl = ft.getLocations().get(0);
					dbFT.addLocation(ftl); // this makes sure foreign keys are setup
					// We don't need to save everything just the new location
					locationRepo.save(ftl);
				}
			}
		} catch (Exception e) {
			/*
			 * if any of the possible problems with getting and parsing this data happens in
			 * production, they will all look the same to the user. It won't matter to them,
			 * something went wrong on the backend and they can't fix it. So catch
			 * everything and throw a new "generic" exception for the user.
			 * 
			 * Note that this exception holds the original exception so that troubleshooting
			 * will have all the details and not just a generic version with no information.
			 */
			throw new UnableToLoadDataException("failed to load data", e);
		}
	}
}
