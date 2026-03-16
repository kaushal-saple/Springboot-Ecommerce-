package com.ecommerce.sb_ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank
    @Size(min = 3, message = "Product name should contain least 3 letters")
    private String productName;
    private String image;

    @NotBlank
    @Size(min = 3, message = "Product description should contain least 6 letters")
    private String description;
    private Integer quantity;
    private Double price;
    private Double discount;
    private Double specialPrice;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;

}
