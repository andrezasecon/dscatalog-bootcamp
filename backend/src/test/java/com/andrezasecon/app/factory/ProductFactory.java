package com.andrezasecon.app.factory;

import java.time.Instant;

import com.andrezasecon.app.dto.ProductDTO;
import com.andrezasecon.app.entities.Category;
import com.andrezasecon.app.entities.Product;

public class ProductFactory {
	
	public static Product createProduct() {
		Product product = new Product(1L, "Phone", "Good Phone", 800.0, "http://img.com/img.png", Instant.parse("2021-06-02T03:00:00Z"));
		product.getCategories().add(new Category(2L, "Electronics"));
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}


}
