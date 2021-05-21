package com.andrezasecon.app.resouces;

import com.andrezasecon.app.dto.CategoryDTO;
import com.andrezasecon.app.services.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class CategoryResourceTest {
    @Mock
    CategoryService categoryService;
    @InjectMocks
    CategoryResource categoryResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

//    @Test
//    void testFindAllCategories() {
//        when(categoryService.findAll()).thenReturn(Arrays.<CategoryDTO>asList(new CategoryDTO(Long.valueOf(1), "Books")));
//
//        ResponseEntity<List<CategoryDTO>> result = categoryResource.findAllCategories();
//        Assertions.assertEquals(200, result);
//    }

    @Test
    void testFindCategoryById() {
        when(categoryService.findCategoryById(anyLong())).thenReturn(new CategoryDTO(Long.valueOf(1), "name"));

        ResponseEntity<CategoryDTO> result = categoryResource.findCategoryById(Long.valueOf(1));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testInsertCategory() {
        when(categoryService.insertCategory(any())).thenReturn(new CategoryDTO(Long.valueOf(1), null));

        ResponseEntity<CategoryDTO> result = categoryResource.insertCategory(new CategoryDTO(Long.valueOf(1), null));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testUpdateCategory() {
        when(categoryService.updateCategory(anyLong(), any())).thenReturn(new CategoryDTO(Long.valueOf(1), "name"));

        ResponseEntity<CategoryDTO> result = categoryResource.updateCategory(Long.valueOf(1), new CategoryDTO(Long.valueOf(1), "name"));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testDeleteCategory() {
        ResponseEntity<CategoryDTO> result = categoryResource.deleteCategory(Long.valueOf(1));
        Assertions.assertEquals(null, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme