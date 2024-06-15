package com.cineplanetfactory.retocp.application.port.input;

import com.cineplanetfactory.retocp.domain.dto.OrderDTO;
import com.cineplanetfactory.retocp.domain.request.OrderSaveReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    OrderDTO findOrderById(Long id);
    OrderDTO saveOrder(OrderSaveReq req);
    OrderDTO updateOrder(OrderSaveReq req);
    void deleteOrder(Long id);
    List<OrderDTO> ListAllOrders();
    Page<OrderDTO> findOrderPage(Pageable pageable);
    OrderDTO getOrderByProductName(String name);
    OrderDTO getOrderByCustomerLastname(String lastname);
}
