package com.cineplanetfactory.retocp.application.service;

import com.cineplanetfactory.retocp.adapters.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductServiceImpl{
    @Autowired // Dependency injection by annotation
    private IProductRepository productRepository;


}
