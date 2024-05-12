package com.jammeraal.sffoodtruck.trucks;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

//FIXME this needs to use a mock service
@SpringBootTest
@AutoConfigureMockMvc
public class FoodTruckControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	void getAllTrucks() throws Exception {
		// data isn't loaded so an empty list should come back
		this.mockMvc.perform(get("/trucks"))
				// .andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(is("[]")));
	}

//	@Test
//	void updateTruckList() throws Exception {
//		// FIXME refactor so we can call this without it actually reaching out to SF
//		this.mockMvc.perform(post("/trucks"))
//				// .andDo(print())
//				.andExpect(status().isOk());
//
//		this.mockMvc.perform(post("/trucks"))
//				.andExpect(status().isBadRequest());
//	}

	@Test
	void idNotFound() throws Exception {
		this.mockMvc.perform(get("/trucks/1"))
				.andExpect(status().isNotFound());
	}
}
