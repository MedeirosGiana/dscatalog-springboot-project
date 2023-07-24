package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests{
    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    private long existingId;
   private long noExistingId;
    private long dependentId;
    /*private PageImpl<Product> page;
    private Product product;*/

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        noExistingId = 2L;
        dependentId = 3L;
        //product = Factory.createProduct();
        //page = new PageImpl<>(List.of(product));


        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(noExistingId)).thenReturn(false);

        Mockito.doNothing().when(repository).deleteById(existingId);

    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenNoExixistId(){
        Assertions.assertThrows(ResourceNotFoundException.class,() -> {
            service.delete(noExistingId);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
        Mockito.verify(repository).deleteById(existingId);
    }

}
