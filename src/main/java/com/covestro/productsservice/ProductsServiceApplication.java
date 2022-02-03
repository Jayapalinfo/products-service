package com.covestro.productsservice;

import com.covestro.productsservice.domain.Product;
import com.covestro.productsservice.service.ProductsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
@Slf4j
public class ProductsServiceApplication {

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
	public static void main(String[] args) {
		SpringApplication.run(ProductsServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner loadProducts(ProductsService productsService) {
		return args -> {
			ObjectMapper mapper = objectMapper();
			TypeReference<List<Product>> typeReference = new TypeReference<>() {
			};
			try(InputStream inputStream = TypeReference.class.getResourceAsStream("/json/products.json")) {
				List<Product> users = mapper.readValue(inputStream, typeReference);
				productsService.saveAll(users);
				log.info("Products Saved");
			} catch (IOException e) {
				log.error("Error: {}", e.getMessage());
			}
		};
	}

}
