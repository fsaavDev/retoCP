package com.cineplanetfactory.retocp.adapters.util;

import com.cineplanetfactory.retocp.domain.dto.ProductDTO;
import com.cineplanetfactory.retocp.domain.model.Product;
import com.cineplanetfactory.retocp.domain.request.ProductSaveReq;

public interface IMapper {
    ProductDTO toDTO(Product e);
    Product toEntity(ProductSaveReq req);
}
