package com.cineplanetfactory.retocp.application.service;

import com.cineplanetfactory.retocp.adapters.repository.IOrderRepository;
import com.cineplanetfactory.retocp.adapters.repository.IProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public class OrderServiceImpl{

    private final IProductRepository productRepository;
    private final IOrderRepository orderRepository;

    /**
     * Dependency Injection por constructor
     * @param productRepository repositorio de Productos
     * @param orderRepository repositorio de Pedidos
     */
    public OrderServiceImpl(IProductRepository productRepository, IOrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

}
