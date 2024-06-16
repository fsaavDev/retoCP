package com.cineplanetfactory.retocp.adapters.util;

import com.cineplanetfactory.retocp.domain.dto.OrderDTO;
import com.cineplanetfactory.retocp.domain.model.Order;
import com.cineplanetfactory.retocp.domain.model.Product;
import com.cineplanetfactory.retocp.domain.request.OrderSaveReq;
import com.cineplanetfactory.retocp.domain.request.ProductSaveReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MapperTest {

    @InjectMocks
    Mapper mapper;

    Product product;

    Order order;

    ProductSaveReq req;

    OrderDTO.CustomerDTO customerDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        //GIVEN
        req = new ProductSaveReq();
        req.setName("Product A");
        req.setPrice(BigDecimal.valueOf(100.00));


        product = new Product();
        product.setName(req.getName());
        product.setCode(1L);
        product.setPrice(req.getPrice());

        order = new Order();
        order.setOrderNumber(1L);
        order.setCustomer(new Order.Customer("Customer A","Lastname A", "Address A"));
        order.setQuantity(5);
        order.setProduct(product);

        customerDTO = new OrderDTO.CustomerDTO();
        customerDTO.setNames("Customer A");
        customerDTO.setLastnames("Lastname A");
        customerDTO.setAddress("Address A");
    }

    @Test
    void toDTO() {
        mapper.toDTO(product);
    }

    @Test
    void toEntity() {
        mapper.toEntity(req);
    }

    @Test
    void ToDTO1() {
        mapper.toDTO(order);
    }
}