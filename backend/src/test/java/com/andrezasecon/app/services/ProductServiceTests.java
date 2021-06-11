package com.andrezasecon.app.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.andrezasecon.app.dto.ProductDTO;
import com.andrezasecon.app.entities.Category;
import com.andrezasecon.app.entities.Product;
import com.andrezasecon.app.factory.CategoryFactory;
import com.andrezasecon.app.factory.ProductFactory;
import com.andrezasecon.app.repositories.CategoryRepository;
import com.andrezasecon.app.repositories.ProductRepository;
import com.andrezasecon.app.services.exceptions.DataBaseException;
import com.andrezasecon.app.services.exceptions.ResourceNotFoundException;
import com.zaxxer.hikari.util.ClockSource.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	// ingectMocks simula a injeção de dependencia da Productservice
	@InjectMocks
	private ProductService productService;
	
	
	// simula comportamento mockado da Categoryrepository
	@Mock
	private ProductRepository productRepository;
	
	
	// simula comportamento mockado da Categoryrepository
	@Mock
	private CategoryRepository categoryRepository;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId; 
	private PageImpl<Product> page;
	private Product product;
	private ProductDTO dto;
	private Category category;

	
	@BeforeEach
	void setUtp() throws Exception {
		
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 4L;
		product = ProductFactory.createProduct();
		category = CategoryFactory.createCategory();
		dto = ProductFactory.createProductDTO();
		page = new PageImpl<>(List.of(product));
		
		
		// --------------------- Mock Repository of deleteProduct ---------------------- //
		
		// Configurando comportamento mockado da repository, doNothing por q p método é void
		Mockito.doNothing().when(productRepository).deleteById(existingId);
		
		// Configurando comportamento mockado da repository, doThrow por q p método lança exception
		Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);
	
		// Configurando comportamento mockado da repository, doThrow por q p método lança exception
		Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);

		
		// --------------------- Mock Repository of FindAllPaged ---------------------- //
 
		Mockito.when(productRepository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		
		// --------------------- Mock Repository of saveProduct ---------------------- //

		Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
		
	
		// --------------------- Mock Repository of FindById ---------------------- //
		
		Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
		
		Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());	
		
		Mockito.doThrow(ResourceNotFoundException.class).when(productRepository).findById(nonExistingId);
		
		Mockito.when(productRepository.getOne(existingId)).thenReturn(product);
		
		Mockito.when(productRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		
		// --------------------- Mock Repository of updateProduct ---------------------- //
		

		Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
		
		Mockito.when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

			
		

	}  
	
	@Test 
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() ->{
			productService.deleteProduct(existingId);

		});
		
		Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldThrowResouceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () ->{
			productService.deleteProduct(nonExistingId);

		});
		
		Mockito.verify(productRepository, Mockito.times(1)).deleteById(nonExistingId);
	}
	
	@Test
	public void deleteShouldThrowDataBaseExceptionWhenIdDependentAnother() {
		
		Assertions.assertThrows(DataBaseException.class, () ->{
			productService.deleteProduct(dependentId);

		});
		
		Mockito.verify(productRepository, Mockito.times(1)).deleteById(dependentId);
	}

	@Test
	public void findAllPagedShouldReturnPage() {
		
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = productService.findAllPaged(pageable);
		
		Assertions.assertNotNull(result);
		Mockito.verify(productRepository, Mockito.times(1)).findAll(pageable);
		
	}
	
	@Test
	public void findByIdShouldReturnProductDtoWhenIdExists() {
		
		ProductDTO dto = productService.findProductById(existingId);
		
		Assertions.assertNotNull(dto);	
		Mockito.verify(productRepository, Mockito.times(1)).findById(existingId);

		
	}
	
	
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () ->{
			productService.findProductById(nonExistingId);
		});
		
	}
	
	@Test
	public void updateProductShouldReturnProductDtoWhenIdExists() {
		
		ProductDTO result = productService.updateProduct(existingId, dto);
		
		Assertions.assertNotNull(result);	
		
	}
	
	@Test
	public void updateProductShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () ->{
			productService.updateProduct(nonExistingId, dto);
		});
		
	}
	
}
