package com.cineplanetfactory.retocp.adapters.util;

import com.cineplanetfactory.retocp.domain.dto.OrderDTO;
import com.cineplanetfactory.retocp.domain.dto.ProductDTO;
import com.cineplanetfactory.retocp.domain.model.Order;
import com.cineplanetfactory.retocp.domain.model.Product;
import com.cineplanetfactory.retocp.domain.request.OrderSaveReq;
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

    @Override
    public OrderDTO toDTO(Order e) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderNumber(e.getOrderNumber());
        dto.setProductCode(e.getProduct().getCode());
        dto.setQuantity(e.getQuantity());
        dto.setCustomer(this.toDTO(e.getCustomer()));
        return dto;
    }

    public OrderDTO.CustomerDTO toDTO(Order.Customer e) {
        OrderDTO.CustomerDTO dto = new OrderDTO.CustomerDTO();
        dto.setNames(e.getNames());
        dto.setLastnames(e.getLastnames());
        dto.setAddress(e.getAddress());
        return dto;
    }
}

