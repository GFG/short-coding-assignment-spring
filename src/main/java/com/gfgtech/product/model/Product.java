package com.gfgtech.product.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String productId;
    private String title;
    private String description;
    private String brand;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;
    private String color;

    public List<String> nullProperties() {

        List<String> nullFields = new ArrayList<>();

        for (Field field : this.getClass().getDeclaredFields()) {

            try {
                if (field.get(this) == null) nullFields.add(field.getName());
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException("Unable to fetch null fields", e);
            }
        }

        return nullFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id)
                && Objects.equals(productId, product.productId)
                && Objects.equals(title, product.title)
                && Objects.equals(description, product.description)
                && Objects.equals(brand, product.brand)
                && price.compareTo(product.price) == 0
                && Objects.equals(color, product.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, title, description, brand, price, color);
    }
}
