package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product createProduct(){
        Product product = new Product(1L,"Phone","Good Phone",800.00,"https://img.com/img.png", Instant.parse("2023-10-20T15:30:20Z"));
        product.getCategories().add(new Category(2L,"electronics"));
        return product;
    }
    public static ProductDTO createProductDTO(){
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }
}
