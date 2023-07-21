package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;
    private long existingId;
    private long noExistingId;

    private long countTotalProducts;

    @BeforeEach
    void setRepository() throws Exception{
        existingId = 1L;
        countTotalProducts = 25L;
        noExistingId = 1000L;
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull(){
        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1,product.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        repository.deleteById(existingId);
        Optional<Product> result = repository.findById(existingId);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void findByIdShouldReturnOptionalProductWhenIdExists(){
        Optional<Product> obj = repository.findById(existingId);
        Assertions.assertEquals(existingId, existingId);
    }

    @Test
    public void findByIdShouldReturnProductEmptyWhenIdNotExists(){
        Optional<Product> obj = repository.findById(noExistingId);
        Assertions.assertFalse(obj.isPresent());
    }

}
