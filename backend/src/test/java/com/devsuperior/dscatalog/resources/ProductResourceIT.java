package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private Long existsId;
    private Long noExistsId;
    private Long countTotalProduct;

    @BeforeEach
    void setUp() throws Exception {
        existsId = 1L;
        noExistsId = 1000L;
        countTotalProduct = 25L;
    }

    @Test
    public void findAllShouldReturnSortPagedWhenSortByName() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(
                        "/products?page=0&size=12&sort=name,asc")
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.totalElements").value(countTotalProduct));
        resultActions.andExpect(jsonPath("$.content").exists());
        resultActions.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
        resultActions.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
        resultActions.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
    }

    @Test
    public void updateShouldReturnProductDTOWhenExistsId() throws Exception {
        ProductDTO productDTO = Factory.createProductDTO();
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put(
                        "/products/{id}", existsId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").value(existsId));
        resultActions.andExpect(jsonPath("$.name").value(productDTO.getName()));
        resultActions.andExpect(jsonPath("$.description").value(productDTO.getDescription()));
        resultActions.andExpect(jsonPath("$.price").value(productDTO.getPrice()));
    }

    @Test
    public void updateShouldReturnNotFoundWhenDoesNonExistsId() throws Exception {
        ProductDTO productDTO = Factory.createProductDTO();
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.put(
                        "/products/{id}", noExistsId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }
}
