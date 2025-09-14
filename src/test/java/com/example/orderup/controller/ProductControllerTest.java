package com.example.orderup.controller;

import com.example.orderup.controlller.ProductController;
import com.example.orderup.entity.Product;
import com.example.orderup.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)   // <-- FIXED: point to controller, not test class
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createProduct_success() throws Exception {
        Product product = new Product("Rare", 5);

        // Inject ID manually with reflection
        Field idField = Product.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(product, 1L);

        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Rare\",\"stock\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rare"))
                .andExpect(jsonPath("$.stock").value(5));
    }

    @Test
    void getAllProducts_success() throws Exception {
        Product p1 = new Product("Rare", 5);

        // Inject ID manually with reflection
        Field idField = Product.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(p1, 1L);

        Mockito.when(productRepository.findAll()).thenReturn(List.of(p1));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Rare"))
                .andExpect(jsonPath("$[0].stock").value(5));
    }
}
