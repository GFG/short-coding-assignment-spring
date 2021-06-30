package com.gfgtech.product.web;

import com.gfgtech.product.model.Product;
import com.gfgtech.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok().body(productRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        return productRepository
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()   );
    }

    @PostMapping
    ResponseEntity<Product> newProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productRepository.save(product));
    }

    @PatchMapping(value = "/{id}")
    ResponseEntity updateProduct(@RequestBody Product product, @PathVariable Long id) {
        return productRepository
                .findById(id)
                .map(
                        existingProduct -> {
                            product.setId(id);
                            BeanUtils.copyProperties(
                                    product,
                                    existingProduct,
                                    product.nullProperties()
                                            .toArray(new String[product.nullProperties().size()]));
                            return ResponseEntity.ok(productRepository.save(existingProduct));
                        })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
