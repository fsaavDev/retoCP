package com.cineplanetfactory.retocp.adapters.repository;

import com.cineplanetfactory.retocp.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface IProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContains(String name);
    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);
}
