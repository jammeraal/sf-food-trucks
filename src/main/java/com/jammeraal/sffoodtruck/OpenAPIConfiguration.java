package com.jammeraal.sffoodtruck;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Setup open specification
 */
@Configuration
public class OpenAPIConfiguration {

	@Bean
	OpenAPI defineOpenApi() {
		Server server = new Server();
		server.setUrl("http://localhost:8081");
		server.setDescription("Development");

		Contact myContact = new Contact();
		myContact.setName("Joe Ammeraal");

		Info information = new Info().title("Food Truck Visitation").version("1.0")
				.description(
						"This API exposes endpoints to find and keep track of your food truck visits. Specifically in the San Francisco area")
				.contact(myContact);
		return new OpenAPI().info(information).servers(List.of(server));
	}
}
