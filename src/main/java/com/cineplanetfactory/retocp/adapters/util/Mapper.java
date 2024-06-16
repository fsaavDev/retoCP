package com.cineplanetfactory.retocp.adapters.util;

import com.cineplanetfactory.retocp.domain.dto.ProductDTO;
import com.cineplanetfactory.retocp.domain.model.Product;
import com.cineplanetfactory.retocp.domain.request.ProductSaveReq;
import org.springframework.stereotype.Component;

@Component
public class Mapper implements IMapper{

    @Override
    public ProductDTO toDTO(Product e) {
        return new ProductDTO(e.getCode(),e.getName(),e.getPrice());
    }

    @Override
    public Product toEntity(ProductSaveReq req) {
        Product product = new Product();
        product.setName(req.getName());
        product.setPrice(req.getPrice());
        return product;
    }
}

