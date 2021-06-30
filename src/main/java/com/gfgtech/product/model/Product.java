package com.gfgtech.product.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
}
