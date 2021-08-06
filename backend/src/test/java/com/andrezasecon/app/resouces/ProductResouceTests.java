package com.andrezasecon.app.resouces;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.andrezasecon.app.dto.ProductDTO;
import com.andrezasecon.app.factory.ProductFactory;
import com.andrezasecon.app.services.ProductService;
import com.andrezasecon.app.services.exceptions.DataBaseException;
import com.andrezasecon.app.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(ProductResource.class)
public class ProductResouceTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@MockBean
	private ProductService service;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private ProductDTO productDTO;
	private PageImpl<ProductDTO> page;	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	
	
	
	@BeforeEach
	void setup() throws Exception {
		
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		
		productDTO = ProductFactory.createProductDTO();
		page = new PageImpl<>(List.of(productDTO));
		
		//when(service.findAllPaged(any())).thenReturn(page);
		
		when(service.findProductById(existingId)).thenReturn(productDTO);
		when(service.findProductById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		when(service.updateProduct(eq(existingId), any())).thenReturn(productDTO);
		when(service.updateProduct(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

		doNothing().when(service).deleteProduct(existingId);
		doThrow(ResourceNotFoundException.class).when(service).deleteProduct(nonExistingId);
		doThrow(DataBaseException.class).when(service).deleteProduct(dependentId);
		
		when(service.insertProduct(any())).thenReturn(productDTO);
		
		
		

	}
	
	@Test
	public void findAllPageableShouldReturnPage() throws Exception {
		
		ResultActions result = mockMvc.perform(get("/products")
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
	}
	
	@Test
	public void findProductByIdShouldReturnProductWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}

	
	@Test
	public void findProductByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
		
	}
	
	@Test
	public void updateProductShouldReturnProductDTOWhenIdExists() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		
		
	}
	
	@Test
	public void updateProductShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	
	@Test
	public void deleteProductShouldDoNothingWhenIdExists() throws Exception {
		
		ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNoContent());
	}
	

	@Test
	public void deleteProductShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		
		ResultActions result = mockMvc.perform(delete("/products/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteProductShouldReturnDataBaseExeptionWhenDependentId() throws Exception {
			
		ResultActions result = mockMvc.perform(delete("/products/{id}", dependentId)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isBadRequest());
	}
	
	
	@Test
	public void insertProductShouldReturnCreatedAndProductDTO() throws Exception {
			
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform(post("/products")
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		
		
	}
	
}



















