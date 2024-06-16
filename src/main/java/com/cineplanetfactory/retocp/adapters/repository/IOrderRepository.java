package com.cineplanetfactory.retocp.adapters.repository;

import com.cineplanetfactory.retocp.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IOrderRepository extends JpaRepository<Order, Long> {
    @Query("FROM Order o WHERE o.product.name = :name")
    List<Order> getByProductName(String name);

    @Query("FROM Order o WHERE o.customer.lastnames LIKE %:lastname%")
    List<Order> getByCustomerLastname(String lastname);
}
