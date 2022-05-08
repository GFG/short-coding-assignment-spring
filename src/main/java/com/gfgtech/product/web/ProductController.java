package com.gfgtech.product.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfgtech.email.EmailSender;
import com.gfgtech.product.model.Product;
import com.gfgtech.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.ServiceUnavailableException;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    public static final String EMAIL_ADMIN = "admin@marketplace.com";
    public static final String EMAIL_SELLER = "seller@marketplace.com";
    private final ProductRepository productRepository;

    private final EmailSender emailSender;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok().body(productRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        return productRepository
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<Product> newProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productRepository.save(product));
    }

    @PatchMapping(value = "/{id}")
    ResponseEntity updateProduct(@RequestBody @Valid Product product, @PathVariable Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Product existingProductCopy = copyProduct(existingProduct);
        validate(product, existingProductCopy);

        Product updatingProduct = existingProduct;
        String[] nullProperties = product.nullProperties().toArray(new String[product.nullProperties().size()]);
        BeanUtils.copyProperties(product, updatingProduct, nullProperties);

        if (existingProductCopy.equals(updatingProduct)) {
            return ResponseEntity.ok().build();
        }

        Product updatedProduct = productRepository.save(updatingProduct);
        sendEmail(updatingProduct, existingProductCopy);

        return ResponseEntity.ok(updatedProduct);
    }

    private void validate(Product product, Product existingProduct) {
        if (product.getPrice() != null
                && product.getPrice().multiply(BigDecimal.valueOf(2)).compareTo(existingProduct.getPrice()) < 0) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "WRONG_PRICE_DIFF");
        }
    }

    private void sendEmail(Product updatingProduct, Product existingProductCopy) {
        Set<String> recipients = new HashSet<>();
        recipients.add(EMAIL_SELLER);
        boolean isPriceChanged = updatingProduct.getPrice().compareTo(existingProductCopy.getPrice()) != 0;
        if (isPriceChanged) {
            recipients.add(EMAIL_ADMIN);
        }

        try {
            emailSender.sendMail("updated product:" + existingProductCopy + "\n new version:" + updatingProduct, recipients);
        } catch (ServiceUnavailableException e) {
            log.error("failed to send email", e);
        }
    }

    private Product copyProduct(Product product) {
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(product), Product.class);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED_TO_COPY");
        }
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
