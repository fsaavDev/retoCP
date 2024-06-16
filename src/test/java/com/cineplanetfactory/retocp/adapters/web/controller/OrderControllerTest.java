package com.cineplanetfactory.retocp.adapters.web.controller;

import com.cineplanetfactory.retocp.adapters.web.exception.Handler;
import com.cineplanetfactory.retocp.application.port.input.IOrderService;
import com.cineplanetfactory.retocp.domain.dto.OrderDTO;
import com.cineplanetfactory.retocp.domain.request.OrderSaveReq;
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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private IOrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new Handler())
                .build();
    }

    @Test
    void findOrderById() throws Exception {
        Long orderId = 1L;
        OrderDTO.CustomerDTO customerDTO = new OrderDTO.CustomerDTO("John", "Doe", "123 Street");
        OrderDTO orderDTO = new OrderDTO(orderId, 1L, 2, customerDTO);


        when(orderService.findOrderById(anyLong())).thenReturn(orderDTO);


        mockMvc.perform(get("/pedidos/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.nro_pedido").value(orderDTO.getOrderNumber()))
                .andExpect(jsonPath("$.data.codigo_producto").value(orderDTO.getProductCode()))
                .andExpect(jsonPath("$.data.cantidad").value(orderDTO.getQuantity()))
                .andExpect(jsonPath("$.data.cliente.nombres").value(orderDTO.getCustomer().getNames()))
                .andExpect(jsonPath("$.data.cliente.apellidos").value(orderDTO.getCustomer().getLastnames()))
                .andExpect(jsonPath("$.data.cliente.direccion").value(orderDTO.getCustomer().getAddress()));
    }

    @Test
    void saveOrder() throws Exception {
        OrderDTO.CustomerDTO customerDTO = new OrderDTO.CustomerDTO("John", "Doe", "123 Street");
        OrderSaveReq orderSaveReq = new OrderSaveReq();
        orderSaveReq.setProductCode(1L);
        orderSaveReq.setQuantity(2);
        orderSaveReq.setNames("John");
        orderSaveReq.setLastnames("Doe");
        orderSaveReq.setAddress("123 Street");
        OrderDTO orderDTO = new OrderDTO(1L, 1L, 2, customerDTO);
        String jsonOrderSaveReq = objectMapper.writeValueAsString(orderSaveReq);


        when(orderService.saveOrder(any())).thenReturn(orderDTO);


        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOrderSaveReq)) // FORMA 2 CON CONVERTIDOR (CONVENIENTE EN CLASE GRANDE)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.nro_pedido").value(orderDTO.getOrderNumber()))
                .andExpect(jsonPath("$.data.codigo_producto").value(orderDTO.getProductCode()))
                .andExpect(jsonPath("$.data.cantidad").value(orderDTO.getQuantity()))
                .andExpect(jsonPath("$.data.cliente.nombres").value(orderDTO.getCustomer().getNames()))
                .andExpect(jsonPath("$.data.cliente.apellidos").value(orderDTO.getCustomer().getLastnames()))
                .andExpect(jsonPath("$.data.cliente.direccion").value(orderDTO.getCustomer().getAddress()));
    }

    @Test
    void updateOrder() throws Exception {
        OrderDTO updatedOrderDTO = new OrderDTO(1L, 1L, 3, new OrderDTO.CustomerDTO("UpdatedName", "UpdatedLastname", "UpdatedAddress"));
        OrderSaveReq orderSaveReq = new OrderSaveReq();
        orderSaveReq.setProductCode(1L);
        orderSaveReq.setQuantity(3);
        orderSaveReq.setNames("UpdatedName");
        orderSaveReq.setLastnames("UpdatedLastname");
        orderSaveReq.setAddress("UpdatedAddress");
        String jsonOrderSaveReq = objectMapper.writeValueAsString(orderSaveReq);


        when(orderService.updateOrder(any(), anyLong())).thenReturn(updatedOrderDTO);


        mockMvc.perform(put("/pedidos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonOrderSaveReq))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.nro_pedido").value(updatedOrderDTO.getOrderNumber()))
                .andExpect(jsonPath("$.data.codigo_producto").value(updatedOrderDTO.getProductCode()))
                .andExpect(jsonPath("$.data.cantidad").value(updatedOrderDTO.getQuantity()))
                .andExpect(jsonPath("$.data.cliente.nombres").value(updatedOrderDTO.getCustomer().getNames()))
                .andExpect(jsonPath("$.data.cliente.apellidos").value(updatedOrderDTO.getCustomer().getLastnames()))
                .andExpect(jsonPath("$.data.cliente.direccion").value(updatedOrderDTO.getCustomer().getAddress()));
    }

    @Test
    void deleteOrder() throws Exception {
        doNothing().when(orderService).deleteOrder(anyLong());


        mockMvc.perform(delete("/pedidos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value("Pedido eliminado"));
    }

    @Test
    void listAllOrders() throws Exception {
        List<OrderDTO> orderList = Arrays.asList(
                new OrderDTO(1L, 1L, 3, new OrderDTO.CustomerDTO("Name1", "Lastname1", "Address1")),
                new OrderDTO(2L, 2L, 4, new OrderDTO.CustomerDTO("Name2", "Lastname2", "Address2"))
        );

        when(orderService.listAllOrders()).thenReturn(orderList);


        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].nro_pedido").value(orderList.get(0).getOrderNumber()))
                .andExpect(jsonPath("$.data[0].codigo_producto").value(orderList.get(0).getProductCode()))
                .andExpect(jsonPath("$.data[0].cantidad").value(orderList.get(0).getQuantity()))
                .andExpect(jsonPath("$.data[0].cliente.nombres").value(orderList.get(0).getCustomer().getNames()))
                .andExpect(jsonPath("$.data[0].cliente.apellidos").value(orderList.get(0).getCustomer().getLastnames()))
                .andExpect(jsonPath("$.data[0].cliente.direccion").value(orderList.get(0).getCustomer().getAddress()))
                .andExpect(jsonPath("$.data[1].nro_pedido").value(orderList.get(1).getOrderNumber()))
                .andExpect(jsonPath("$.data[1].codigo_producto").value(orderList.get(1).getProductCode()))
                .andExpect(jsonPath("$.data[1].cantidad").value(orderList.get(1).getQuantity()))
                .andExpect(jsonPath("$.data[1].cliente.nombres").value(orderList.get(1).getCustomer().getNames()))
                .andExpect(jsonPath("$.data[1].cliente.apellidos").value(orderList.get(1).getCustomer().getLastnames()))
                .andExpect(jsonPath("$.data[1].cliente.direccion").value(orderList.get(1).getCustomer().getAddress()));
    }

    @Test
    void findOrderPage() throws Exception {
        Page<OrderDTO> orderPage = new PageImpl<>(Arrays.asList(
                new OrderDTO(1L, 1L, 3, new OrderDTO.CustomerDTO("Name1", "Lastname1", "Address1")),
                new OrderDTO(2L, 2L, 4, new OrderDTO.CustomerDTO("Name2", "Lastname2", "Address2"))
        ));


        when(orderService.findOrderPage(any(PageRequest.class))).thenReturn(orderPage);


        mockMvc.perform(get("/pedidos/pagination?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].nro_pedido").value(orderPage.getContent().get(0).getOrderNumber()))
                .andExpect(jsonPath("$.content[0].codigo_producto").value(orderPage.getContent().get(0).getProductCode()))
                .andExpect(jsonPath("$.content[0].cantidad").value(orderPage.getContent().get(0).getQuantity()))
                .andExpect(jsonPath("$.content[0].cliente.nombres").value(orderPage.getContent().get(0).getCustomer().getNames()))
                .andExpect(jsonPath("$.content[0].cliente.apellidos").value(orderPage.getContent().get(0).getCustomer().getLastnames()))
                .andExpect(jsonPath("$.content[0].cliente.direccion").value(orderPage.getContent().get(0).getCustomer().getAddress()))
                .andExpect(jsonPath("$.content[1].nro_pedido").value(orderPage.getContent().get(1).getOrderNumber()))
                .andExpect(jsonPath("$.content[1].codigo_producto").value(orderPage.getContent().get(1).getProductCode()))
                .andExpect(jsonPath("$.content[1].cantidad").value(orderPage.getContent().get(1).getQuantity()))
                .andExpect(jsonPath("$.content[1].cliente.nombres").value(orderPage.getContent().get(1).getCustomer().getNames()))
                .andExpect(jsonPath("$.content[1].cliente.apellidos").value(orderPage.getContent().get(1).getCustomer().getLastnames()))
                .andExpect(jsonPath("$.content[1].cliente.direccion").value(orderPage.getContent().get(1).getCustomer().getAddress()));
    }

    @Test
    void sortAllOrdersByQuantity() throws Exception {
        List<OrderDTO> sortedOrderList = Arrays.asList(
                new OrderDTO(2L, 2L, 4, new OrderDTO.CustomerDTO("Name2", "Lastname2", "Address2")),
                new OrderDTO(1L, 1L, 3, new OrderDTO.CustomerDTO("Name1", "Lastname1", "Address1"))
        );


        when(orderService.sortAllOrdersByQuantity()).thenReturn(sortedOrderList);


        mockMvc.perform(get("/pedidos/order/quantity"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].nro_pedido").value(sortedOrderList.get(0).getOrderNumber()))
                .andExpect(jsonPath("$.data[0].codigo_producto").value(sortedOrderList.get(0).getProductCode()))
                .andExpect(jsonPath("$.data[0].cantidad").value(sortedOrderList.get(0).getQuantity()))
                .andExpect(jsonPath("$.data[0].cliente.nombres").value(sortedOrderList.get(0).getCustomer().getNames()))
                .andExpect(jsonPath("$.data[0].cliente.apellidos").value(sortedOrderList.get(0).getCustomer().getLastnames()))
                .andExpect(jsonPath("$.data[0].cliente.direccion").value(sortedOrderList.get(0).getCustomer().getAddress()))
                .andExpect(jsonPath("$.data[1].nro_pedido").value(sortedOrderList.get(1).getOrderNumber()))
                .andExpect(jsonPath("$.data[1].codigo_producto").value(sortedOrderList.get(1).getProductCode()))
                .andExpect(jsonPath("$.data[1].cantidad").value(sortedOrderList.get(1).getQuantity()))
                .andExpect(jsonPath("$.data[1].cliente.nombres").value(sortedOrderList.get(1).getCustomer().getNames()))
                .andExpect(jsonPath("$.data[1].cliente.apellidos").value(sortedOrderList.get(1).getCustomer().getLastnames()))
                .andExpect(jsonPath("$.data[1].cliente.direccion").value(sortedOrderList.get(1).getCustomer().getAddress()));
    }

    @Test
    void getOrderByOrderName() throws Exception {
        List<OrderDTO> orderList = Arrays.asList(
                new OrderDTO(1L, 1L, 3, new OrderDTO.CustomerDTO("Name1", "Lastname1", "Address1")),
                new OrderDTO(2L, 2L, 4, new OrderDTO.CustomerDTO("Name2", "Lastname2", "Address2"))
        );

        String productName = "product";
        when(orderService.getOrderByProductName(productName)).thenReturn(orderList);

        mockMvc.perform(get("/pedidos/product-name/{name}", productName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].nro_pedido").value(orderList.get(0).getOrderNumber()))
                .andExpect(jsonPath("$.data[0].codigo_producto").value(orderList.get(0).getProductCode()))
                .andExpect(jsonPath("$.data[0].cantidad").value(orderList.get(0).getQuantity()))
                .andExpect(jsonPath("$.data[0].cliente.nombres").value(orderList.get(0).getCustomer().getNames()))
                .andExpect(jsonPath("$.data[0].cliente.apellidos").value(orderList.get(0).getCustomer().getLastnames()))
                .andExpect(jsonPath("$.data[0].cliente.direccion").value(orderList.get(0).getCustomer().getAddress()))
                .andExpect(jsonPath("$.data[1].nro_pedido").value(orderList.get(1).getOrderNumber()))
                .andExpect(jsonPath("$.data[1].codigo_producto").value(orderList.get(1).getProductCode()))
                .andExpect(jsonPath("$.data[1].cantidad").value(orderList.get(1).getQuantity()))
                .andExpect(jsonPath("$.data[1].cliente.nombres").value(orderList.get(1).getCustomer().getNames()))
                .andExpect(jsonPath("$.data[1].cliente.apellidos").value(orderList.get(1).getCustomer().getLastnames()))
                .andExpect(jsonPath("$.data[1].cliente.direccion").value(orderList.get(1).getCustomer().getAddress()));
    }

    @Test
    void getOrderByCustomerLastname() throws Exception {
        List<OrderDTO> orderList = Arrays.asList(
                new OrderDTO(1L, 1L, 3, new OrderDTO.CustomerDTO("Name1", "Lastname1", "Address1")),
                new OrderDTO(2L, 2L, 4, new OrderDTO.CustomerDTO("Name2", "Lastname2", "Address2"))
        );

        String customerLastname = "lastname";
        when(orderService.getOrderByCustomerLastname(customerLastname)).thenReturn(orderList);

        mockMvc.perform(get("/pedidos/customer-lastname/{lastname}", customerLastname))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].nro_pedido").value(orderList.get(0).getOrderNumber()))
                .andExpect(jsonPath("$.data[0].codigo_producto").value(orderList.get(0).getProductCode()))
                .andExpect(jsonPath("$.data[0].cantidad").value(orderList.get(0).getQuantity()))
                .andExpect(jsonPath("$.data[0].cliente.nombres").value(orderList.get(0).getCustomer().getNames()))
                .andExpect(jsonPath("$.data[0].cliente.apellidos").value(orderList.get(0).getCustomer().getLastnames()))
                .andExpect(jsonPath("$.data[0].cliente.direccion").value(orderList.get(0).getCustomer().getAddress()))
                .andExpect(jsonPath("$.data[1].nro_pedido").value(orderList.get(1).getOrderNumber()))
                .andExpect(jsonPath("$.data[1].codigo_producto").value(orderList.get(1).getProductCode()))
                .andExpect(jsonPath("$.data[1].cantidad").value(orderList.get(1).getQuantity()))
                .andExpect(jsonPath("$.data[1].cliente.nombres").value(orderList.get(1).getCustomer().getNames()))
                .andExpect(jsonPath("$.data[1].cliente.apellidos").value(orderList.get(1).getCustomer().getLastnames()))
                .andExpect(jsonPath("$.data[1].cliente.direccion").value(orderList.get(1).getCustomer().getAddress()));
    }
}