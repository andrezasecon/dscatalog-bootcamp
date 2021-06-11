package com.andrezasecon.app.resouces;

import com.andrezasecon.app.dto.CategoryDTO;
import com.andrezasecon.app.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> findAllCategories(Pageable pageable) {
        Page<CategoryDTO> list = categoryService.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> findCategoryById(@PathVariable Long id) {
        CategoryDTO dto = categoryService.findCategoryById(id);
        return ResponseEntity.ok().body(dto);

    }

    @PostMapping
    public ResponseEntity<CategoryDTO> insertCategory(@RequestBody CategoryDTO dto) {
        dto = categoryService.insertCategory(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        dto = categoryService.updateCategory(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }


}
