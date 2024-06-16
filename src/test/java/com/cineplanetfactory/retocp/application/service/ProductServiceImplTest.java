package com.cineplanetfactory.retocp.application.service;

import com.cineplanetfactory.retocp.adapters.repository.IProductRepository;
import com.cineplanetfactory.retocp.adapters.util.IMapper;
import com.cineplanetfactory.retocp.adapters.web.exception.ModelNotFoundException;
import com.cineplanetfactory.retocp.domain.dto.ProductDTO;
import com.cineplanetfactory.retocp.domain.model.Product;
import com.cineplanetfactory.retocp.domain.request.ProductSaveReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class ProductServiceImplTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private IMapper mapper;

    @InjectMocks
    private ProductServiceImpl productService;

    Long productId;
    Product product;
    Product product2;
    List<Product> products;
    ProductDTO dto;

    ProductDTO dto2;
    ProductSaveReq req;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);


        //GIVEN
        req = new ProductSaveReq();
        req.setName("Product A");
        req.setPrice(BigDecimal.valueOf(100.00));

        productId = 1L;
        product = new Product();
        product.setCode(productId);
        product.setPrice(req.getPrice());

        product2 = new Product(2L, "Product B", BigDecimal.valueOf(150.00));
        products = Arrays.asList(product, product2);

        dto2 = new ProductDTO(2L, "Product B", BigDecimal.valueOf(150.00));

        dto = new ProductDTO();
        dto.setCode(productId);
        dto.setPrice(req.getPrice());
    }

    @Test
    void findProductById() {
        //WHEN
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(mapper.toDTO(product)).thenReturn(dto);
        ProductDTO result = productService.findProductById(productId);
        //THEN
        assertNotNull(result);
        assertEquals(productId, result.getCode());
    }

    @Test
    public void findProductById_NotFound() {
        //WHEN
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        //THEN
        assertThrows(ModelNotFoundException.class, () -> productService.findProductById(productId));
    }

    @Test
    void saveProduct() {
        //WHEN
        when(productRepository.save(any())).thenReturn(product);
        when(mapper.toDTO(product)).thenReturn(dto);
        ProductDTO result = productService.saveProduct(req);
        //THEN
        assertNotNull(result);
        assertEquals(productId, result.getCode());
    }

    @Test
    void updateProduct() {
        //WHEN
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);
        when(mapper.toDTO(product)).thenReturn(dto);
        ProductDTO result = productService.updateProduct(req,productId);
        //THEN
        assertNotNull(result);
        assertEquals(productId, result.getCode());
    }

    @Test
    public void updateProduct_NotFound() {
        //WHEN
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        //THEN
        assertThrows(ModelNotFoundException.class, () -> productService.updateProduct(req, productId));
    }

    @Test
    void deleteProduct() {
        //WHEN
        when(productRepository.existsById(productId)).thenReturn(true);
        //THEN
        assertDoesNotThrow(()->productService.deleteProduct(productId));
    }

    @Test
    public void deleteProduct_NotFound() {
        //WHEN
        when(productRepository.existsById(productId)).thenReturn(false);

        //THEN
        assertThrows(ModelNotFoundException.class, () -> productService.deleteProduct(productId));
    }

    @Test
    void listAllProducts() {
        //WHEN
        when(productRepository.findAll()).thenReturn(products);
        when(mapper.toDTO(products.get(0))).thenReturn(dto);
        when(mapper.toDTO(products.get(1))).thenReturn(dto2);
        List<ProductDTO> result = productService.ListAllProducts();

        //THEN
        assertNotNull(result);
        assertEquals(products.size(), result.size());
        assertEquals(dto.getName(), result.get(0).getName());
        assertEquals(dto2.getName(), result.get(1).getName());
    }

    @Test
    void orderAllProductsByPrice() {
        //WHEN
        when(productRepository.findAll(Sort.by("price"))).thenReturn(products);
        when(mapper.toDTO(products.get(0))).thenReturn(dto);
        when(mapper.toDTO(products.get(1))).thenReturn(dto2);
        List<ProductDTO> result = productService.OrderAllProductsByPrice();

        //THEN
        assertNotNull(result);
        assertEquals(products.size(), result.size());
        assertEquals(dto.getPrice(), result.get(0).getPrice());
        assertEquals(dto2.getPrice(), result.get(1).getPrice());
    }

    @Test
    void findProductPage() {

        //GIVEN
        Page<Product> productPage = new PageImpl<>(products);
        Pageable pageable = PageRequest.of(0, 10);

        //WHEN
        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(mapper.toDTO(products.get(0))).thenReturn(dto);
        when(mapper.toDTO(products.get(1))).thenReturn(dto2);
        Page<ProductDTO> result = productService.findProductPage(pageable);

        //THEN
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(dto.getName(), result.getContent().get(0).getName());
        assertEquals(dto2.getName(), result.getContent().get(1).getName());
    }

    @Test
    void findProductByName() {

        //GIVEN
        String productName = "Product";

        //WHEN
        when(productRepository.findByNameContains(productName)).thenReturn(products);
        when(mapper.toDTO(products.get(0))).thenReturn(dto);
        when(mapper.toDTO(products.get(1))).thenReturn(dto2);
        List<ProductDTO> result = productService.findProductByName(productName);

        //THEN
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(dto.getName(), result.get(0).getName());
        assertEquals(dto2.getName(), result.get(1).getName());
    }

    @Test
    void findProductByPrice() {

        //GIVEN
        BigDecimal minPrice = BigDecimal.valueOf(100.00);
        BigDecimal maxPrice = BigDecimal.valueOf(200.00);

        //WHEN
        when(productRepository.findByPriceBetween(minPrice, maxPrice)).thenReturn(products);
        when(mapper.toDTO(products.get(0))).thenReturn(dto);
        when(mapper.toDTO(products.get(1))).thenReturn(dto2);
        List<ProductDTO> result = productService.findProductByPrice(minPrice, maxPrice);

        //THEN
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(dto.getName(), result.get(0).getName());
        assertEquals(dto2.getName(), result.get(1).getName());
    }
}