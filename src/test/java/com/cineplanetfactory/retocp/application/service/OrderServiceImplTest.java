package com.cineplanetfactory.retocp.application.service;

import com.cineplanetfactory.retocp.adapters.repository.IOrderRepository;
import com.cineplanetfactory.retocp.adapters.repository.IProductRepository;
import com.cineplanetfactory.retocp.adapters.util.IMapper;
import com.cineplanetfactory.retocp.adapters.web.exception.ModelNotFoundException;
import com.cineplanetfactory.retocp.domain.dto.OrderDTO;
import com.cineplanetfactory.retocp.domain.model.Order;
import com.cineplanetfactory.retocp.domain.model.Product;
import com.cineplanetfactory.retocp.domain.request.OrderSaveReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OrderServiceImplTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IMapper mapper;

    @InjectMocks
    private OrderServiceImpl orderService;


    Long orderId;
    Order order;
    OrderDTO dto;
    OrderSaveReq req;
    Product product;

    Order order2;
    OrderDTO dto2;

    List<Order> orders;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);


        //GIVEN
        orderId = 1L;

        req = new OrderSaveReq();
        req.setProductCode(1L);
        req.setNames("Customer A");
        req.setLastnames("Lastname A");
        req.setAddress("Address A");
        req.setQuantity(5);


        product = new Product();
        product.setCode(1L);

        order = new Order();
        order.setOrderNumber(orderId);
        order.setCustomer(new Order.Customer(req.getNames(), req.getLastnames(), req.getAddress()));
        order.setQuantity(req.getQuantity());

        order2 = new Order();
        order2.setOrderNumber(2L);
        order2.setQuantity(1);

        orders = Arrays.asList(order, order2);

        dto = new OrderDTO();
        dto.setOrderNumber(orderId);
        dto.setProductCode(product.getCode());
        dto.setQuantity(req.getQuantity());
        OrderDTO.CustomerDTO customerDTO = new OrderDTO.CustomerDTO();
        customerDTO.setNames(req.getNames());
        customerDTO.setLastnames(req.getLastnames());
        customerDTO.setAddress(req.getAddress());
        dto.setCustomer(customerDTO);

        dto2 = new OrderDTO();
        dto2.setOrderNumber(2L);
        dto2.setQuantity(1);

    }

    @Test
    void findOrderById() {

        //WHEN
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(mapper.toDTO(order)).thenReturn(dto);
        OrderDTO result = orderService.findOrderById(orderId);
        //THEN
        assertNotNull(result);
        assertEquals(orderId, result.getOrderNumber());
    }

    @Test
    void findOrderById_NotFound() {

        //WHEN
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        //THEN
        assertThrows(ModelNotFoundException.class, () -> orderService.findOrderById(orderId));
    }

    @Test
    void saveOrder() {
        //WHEN
        when(productRepository.findById(req.getProductCode())).thenReturn(Optional.of(product));
        when(orderRepository.save(any())).thenReturn(order);
        when(mapper.toDTO(order)).thenReturn(dto);
        OrderDTO result = orderService.saveOrder(req);
        //THEN

        assertNotNull(result);
        assertEquals(1L, result.getOrderNumber());
        assertEquals(req.getProductCode(), result.getProductCode());
        assertEquals(req.getQuantity(), result.getQuantity());
        assertEquals(req.getNames(), result.getCustomer().getNames());
        assertEquals(req.getLastnames(), result.getCustomer().getLastnames());
        assertEquals(req.getAddress(), result.getCustomer().getAddress());

    }

    @Test
    public void saveOrder_ProductNotFound() {
        //WHEN
        when(productRepository.findById(req.getProductCode())).thenReturn(Optional.empty());

        //THEN
        assertThrows(ModelNotFoundException.class, () -> orderService.saveOrder(req));
    }

    @Test
    void updateOrder() {
        //WHEN
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(mapper.toDTO(order)).thenReturn(dto);
        OrderDTO result = orderService.updateOrder(req, orderId);

        //THEN
        assertNotNull(result);
        assertEquals(orderId, result.getOrderNumber());
        assertEquals(req.getNames(), result.getCustomer().getNames());
        assertEquals(req.getLastnames(), result.getCustomer().getLastnames());
        assertEquals(req.getAddress(), result.getCustomer().getAddress());
        assertEquals(req.getQuantity(), result.getQuantity());
    }

    @Test
    public void updateOrder_NotFound() {
        //WHEN
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        //THEN
        assertThrows(ModelNotFoundException.class, () -> orderService.updateOrder(req, orderId));
    }

    @Test
    void deleteOrder() {
        //WHEN
        when(orderRepository.existsById(orderId)).thenReturn(true);
        //THEN
        assertDoesNotThrow(()->orderService.deleteOrder(orderId));
    }

    @Test
    void deleteOrder_NotDound() {
        //WHEN
        when(orderRepository.existsById(orderId)).thenReturn(false);

        //THEN
        assertThrows(ModelNotFoundException.class, () -> orderService.deleteOrder(orderId));
    }

    @Test
    void listAllOrders() {
        //WHEN
        when(orderRepository.findAll()).thenReturn(orders);
        when(mapper.toDTO(order)).thenReturn(dto);
        when(mapper.toDTO(order2)).thenReturn(dto2);
        List<OrderDTO> result = orderService.listAllOrders();

        //THEN
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getOrderNumber());
        assertEquals(2L, result.get(1).getOrderNumber());
    }

    @Test
    void sortAllOrdersByQuantity() {

        //GIVEN
        List<Order> orders2 = Arrays.asList(order2, order); // ORDENADOS MANUALMENTE

        //WHEN
        when(orderRepository.findAll(Sort.by("quantity"))).thenReturn(orders2);
        when(mapper.toDTO(order)).thenReturn(dto);
        when(mapper.toDTO(order2)).thenReturn(dto2);
        List<OrderDTO> result = orderService.sortAllOrdersByQuantity(); // EL SEGUNDO ES EL MENOR

        //THEN
        assertNotNull(result);
        assertEquals(orders2.size(), result.size());
        assertEquals(2L, result.get(0).getOrderNumber());
        assertEquals(1L, result.get(1).getOrderNumber());
    }

    @Test
    void findOrderPage() {
        //GIVEN
        Pageable pageable = PageRequest.of(0, 5);
        Page<Order> orderPage = new PageImpl<>(orders);

        //WHEN
        when(orderRepository.findAll(pageable)).thenReturn(orderPage);
        when(mapper.toDTO(orders.get(0))).thenReturn(dto);
        when(mapper.toDTO(orders.get(1))).thenReturn(dto2);
        Page<OrderDTO> result = orderService.findOrderPage(pageable);

        //THEN
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getOrderNumber());
        assertEquals(2L, result.getContent().get(1).getOrderNumber());

    }

    @Test
    void getOrderByProductName() {
        //GIVEN
        String productName = "Product A";

        //WHEN
        when(orderRepository.getByProductName(productName)).thenReturn(List.of(order));
        when(mapper.toDTO(order)).thenReturn(dto);
        List<OrderDTO> result = orderService.getOrderByProductName(productName);

        //THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getOrderNumber());
    }

    @Test
    void getOrderByCustomerLastname() {
        //GIVEN
        String customerLastname = "Lastname A";

        //WHEN
        when(orderRepository.getByCustomerLastname(customerLastname)).thenReturn(List.of(order));
        when(mapper.toDTO(order)).thenReturn(dto);
        List<OrderDTO> result = orderService.getOrderByCustomerLastname(customerLastname);

        //THEN
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getOrderNumber());
    }
}