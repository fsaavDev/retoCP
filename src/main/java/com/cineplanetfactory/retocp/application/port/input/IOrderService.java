package com.cineplanetfactory.retocp.application.port.input;

import com.cineplanetfactory.retocp.domain.dto.OrderDTO;
import com.cineplanetfactory.retocp.domain.request.OrderSaveReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    OrderDTO findOrderById(Long id);
    OrderDTO saveOrder(OrderSaveReq req);
    OrderDTO updateOrder(OrderSaveReq req, Long id);
    void deleteOrder(Long id);
    List<OrderDTO> listAllOrders();
    List<OrderDTO> sortAllOrdersByQuantity();
    Page<OrderDTO> findOrderPage(Pageable pageable);
    List<OrderDTO> getOrderByProductName(String name);
    List<OrderDTO> getOrderByCustomerLastname(String lastname);
}
