package com.rc.tech.productservice;

import com.rc.tech.productservice.dto.ProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
class ProductServiceApplicationTests {

	@Container
	private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer<>("mysql:5.7.34")
			.withUsername("Ratnesh2810")
			.withPassword("R@tnesh2810")
			.withDatabaseName("test");

	ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
		dynamicPropertyRegistry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
		dynamicPropertyRegistry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
	}
	@Test
	void shouldCreateProduct() throws Exception {

		ProductRequest productRequest = getProductRequest();
		String productRequestAsString = objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestAsString)
		).andExpect(status().isCreated());

	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("iPhone 14 RED")
				.description("Latest IPhone of Red Color")
				.price(BigDecimal.valueOf(70000))
				.build();
	}

}
