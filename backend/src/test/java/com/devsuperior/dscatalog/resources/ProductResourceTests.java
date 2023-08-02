package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService service;

    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;
    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    @BeforeEach
    void setUp() throws Exception{
        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);

        when(service.findById(existingId)).thenReturn(productDTO);
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(service.update(eq(existingId),any())).thenReturn(productDTO);
        when(service.update(eq(nonExistingId),any())).thenThrow(ResourceNotFoundException.class);

        doNothing().when(service).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
        doThrow(DatabaseException.class).when(service).delete(dependentId);

        when(service.insert(productDTO)).thenReturn(productDTO);
    }

    @Test
    public void insertShouldReturnProductDTOAndCreated()throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(
                "/products", productDTO)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isCreated());
    }
    @Test
    public void deleteShouldReturnNoContentWhenExistsId() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(
                "/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenExistsId() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(
                "/products/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductDTOWhenExistsId() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put(
                "/products/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
        resultActions.andExpect(jsonPath("$.description").exists());

    }

    @Test
    public void updateShouldNotFounWhenNonExistsId() throws  Exception{
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put(
                        "/products/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(
                "/products").accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductWhenExistsId() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(
                "/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
        resultActions.andExpect(jsonPath("$.description").exists());
    }
    @Test
    public void findByIdReturnNotFoundWhenNonExistsId() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(
                "/products/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }
}
