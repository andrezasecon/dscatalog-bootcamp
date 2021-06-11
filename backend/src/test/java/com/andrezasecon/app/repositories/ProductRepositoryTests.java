package com.andrezasecon.app.repositories;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.andrezasecon.app.entities.Product;
import com.andrezasecon.app.factory.ProductFactory;
import com.andrezasecon.app.services.exceptions.ResourceNotFoundException;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;
	
	@BeforeEach
	void setup() throws Exception {
		
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L; 
		
	}
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		Product product = ProductFactory.createProduct();
		product.setId(null);
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}
	
	@Test
	public void findByIdShouldReturnNonEmptyOptionalWhenIdExists() {		
				
		Optional<Product> prod = repository.findById(existingId);
		
		Assertions.assertTrue(prod.isPresent());
		Assertions.assertEquals(prod.get().getName(), "The Lord of the Rings");
		
		
	}
	
	@Test
	public void findByIdShouldDontReturnObjetWhenIdDoesNotExists() {
				
		Optional<Product> prod = repository.findById(nonExistingId);
		
		Assertions.assertTrue(prod.isEmpty());
		
		
	}
	
	
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		repository.deleteById(existingId);
		
		Optional<Product> result = repository.findById(existingId);
		
		Assertions.assertFalse(result.isPresent());
		
	}
	
		
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
				
		Assertions.assertThrows(EmptyResultDataAccessException.class, () ->{
			repository.deleteById(nonExistingId);			
		} );
		
		
		
	}
	

}
