package com.cineplanetfactory.retocp.adapters.web.controller;

import com.cineplanetfactory.retocp.adapters.web.exception.Handler;
import com.cineplanetfactory.retocp.application.port.input.IProductService;
import com.cineplanetfactory.retocp.domain.dto.ProductDTO;
import com.cineplanetfactory.retocp.domain.request.ProductSaveReq;
import com.cineplanetfactory.retocp.domain.response.RetoCpApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private IProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new Handler())
                .build();
    }

    @Test
    void findProductById() throws Exception {
        //GIVEN
        Long productId = 1L;
        ProductDTO productDTO = new ProductDTO(productId, "Test Product", new BigDecimal("10.0"));

        //WHEN
        when(productService.findProductById(productId)).thenReturn(productDTO);

        //THEN
        mockMvc.perform(get("/productos/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.codigo").value(productDTO.getCode()))
                .andExpect(jsonPath("$.data.nombre").value(productDTO.getName()))
                .andExpect(jsonPath("$.data.precio").value(productDTO.getPrice()));
    }

    @Test
    void saveProduct() throws Exception {
        ProductSaveReq productSaveReq = new ProductSaveReq();
        productSaveReq.setName("Test Product");
        productSaveReq.setPrice(new BigDecimal("10.0"));
        ProductDTO savedProductDTO = new ProductDTO(1L, "Test Product", new BigDecimal("10.0"));

        when(productService.saveProduct(any())).thenReturn(savedProductDTO);


        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Test Product\",\"precio\":10.0}") // FORMA 1 JSON PURO
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.codigo").value(savedProductDTO.getCode()))
                .andExpect(jsonPath("$.data.nombre").value(savedProductDTO.getName()))
                .andExpect(jsonPath("$.data.precio").value(savedProductDTO.getPrice()));
    }

    @Test
    void updateProduct() throws Exception {
        Long productId = 1L;
        ProductSaveReq productSaveReq = new ProductSaveReq();
        productSaveReq.setName("Test Product");
        productSaveReq.setPrice(new BigDecimal("15.0"));
        ProductDTO updatedProductDTO = new ProductDTO(productId, "Test Product", new BigDecimal("15.0"));


        when(productService.updateProduct(any(ProductSaveReq.class), eq(productId))).thenReturn(updatedProductDTO);


        mockMvc.perform(put("/productos/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Test Product\",\"precio\":15.0}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.codigo").value(updatedProductDTO.getCode()))
                .andExpect(jsonPath("$.data.nombre").value(updatedProductDTO.getName()))
                .andExpect(jsonPath("$.data.precio").value(updatedProductDTO.getPrice()));
    }

    @Test
    void deleteProduct() throws Exception {
        Long productId = 1L;
        doNothing().when(productService).deleteProduct(productId);

        mockMvc.perform(delete("/productos/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value("Producto eliminado"));
    }

    @Test
    void listAllProducts() throws Exception {

        List<ProductDTO> productList = new ArrayList<>();
        productList.add(new ProductDTO(1L, "Product 1", new BigDecimal("10.0")));
        productList.add(new ProductDTO(2L, "Product 2", new BigDecimal("20.0")));


        when(productService.ListAllProducts()).thenReturn(productList);


        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].codigo").value(productList.get(0).getCode()))
                .andExpect(jsonPath("$.data[0].nombre").value(productList.get(0).getName()))
                .andExpect(jsonPath("$.data[0].precio").value(productList.get(0).getPrice()))
                .andExpect(jsonPath("$.data[1].codigo").value(productList.get(1).getCode()))
                .andExpect(jsonPath("$.data[1].nombre").value(productList.get(1).getName()))
                .andExpect(jsonPath("$.data[1].precio").value(productList.get(1).getPrice()));
    }


    @Test
    void findProductPage() throws Exception {
        List<ProductDTO> productList = new ArrayList<>();
        productList.add(new ProductDTO(1L, "Product 1", new BigDecimal("10.0")));
        productList.add(new ProductDTO(2L, "Product 2", new BigDecimal("20.0")));
        Page<ProductDTO> page = new PageImpl<>(productList);


        when(productService.findProductPage(any(PageRequest.class))).thenReturn(page);


        mockMvc.perform(get("/productos/pagination")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].codigo").value(productList.get(0).getCode()))
                .andExpect(jsonPath("$.content[0].nombre").value(productList.get(0).getName()))
                .andExpect(jsonPath("$.content[0].precio").value(productList.get(0).getPrice()))
                .andExpect(jsonPath("$.content[1].codigo").value(productList.get(1).getCode()))
                .andExpect(jsonPath("$.content[1].nombre").value(productList.get(1).getName()))
                .andExpect(jsonPath("$.content[1].precio").value(productList.get(1).getPrice()));
    }

    @Test
    void findProductByName() throws Exception {
        String productName = "Test Product";
        List<ProductDTO> productList = new ArrayList<>();
        productList.add(new ProductDTO(1L, productName, new BigDecimal("10.0")));


        when(productService.findProductByName(anyString())).thenReturn(productList);


        mockMvc.perform(get("/productos/name/{name}", productName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].codigo").value(productList.get(0).getCode()))
                .andExpect(jsonPath("$.data[0].nombre").value(productName))
                .andExpect(jsonPath("$.data[0].precio").value(productList.get(0).getPrice()));
    }

    @Test
    void findProductByPrice() throws Exception {
        BigDecimal minPrice = new BigDecimal("5.0");
        BigDecimal maxPrice = new BigDecimal("15.0");
        List<ProductDTO> productList = new ArrayList<>();
        productList.add(new ProductDTO(1L, "Product 1", new BigDecimal("10.0")));
        productList.add(new ProductDTO(2L, "Product 2", new BigDecimal("15.0")));


        when(productService.findProductByPrice(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(productList);


        mockMvc.perform(get("/productos/price")
                        .param("min", minPrice.toString())
                        .param("max", maxPrice.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].codigo").value(productList.get(0).getCode()))
                .andExpect(jsonPath("$.data[0].nombre").value(productList.get(0).getName()))
                .andExpect(jsonPath("$.data[0].precio").value(productList.get(0).getPrice()))
                .andExpect(jsonPath("$.data[1].codigo").value(productList.get(1).getCode()))
                .andExpect(jsonPath("$.data[1].nombre").value(productList.get(1).getName()))
                .andExpect(jsonPath("$.data[1].precio").value(productList.get(1).getPrice()));
    }

    @Test
    void orderAllProductsByPrice() throws Exception {
        List<ProductDTO> productList = new ArrayList<>();
        productList.add(new ProductDTO(1L, "Product 1", new BigDecimal("20.0")));
        productList.add(new ProductDTO(2L, "Product 2", new BigDecimal("10.0")));


        when(productService.OrderAllProductsByPrice()).thenReturn(productList);


        mockMvc.perform(get("/productos/order/price"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].codigo").value(productList.get(0).getCode()))
                .andExpect(jsonPath("$.data[0].nombre").value(productList.get(0).getName()))
                .andExpect(jsonPath("$.data[0].precio").value(productList.get(0).getPrice()))
                .andExpect(jsonPath("$.data[1].codigo").value(productList.get(1).getCode()))
                .andExpect(jsonPath("$.data[1].nombre").value(productList.get(1).getName()))
                .andExpect(jsonPath("$.data[1].precio").value(productList.get(1).getPrice()));
    }
}