package com.andrezasecon.app.resouces;

import com.andrezasecon.app.dto.ProductDTO;
import com.andrezasecon.app.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAllProducts(Pageable pageable) {
        Page<ProductDTO> list = productService.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findProductById(@PathVariable Long id) {
        ProductDTO dto = productService.findProductById(id);
        return ResponseEntity.ok().body(dto);

    }

    @PostMapping
    public ResponseEntity<ProductDTO> insertProduct(@RequestBody ProductDTO dto) {
        dto = productService.insertProduct(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO dto) {
        dto = productService.updateProduct(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


}
