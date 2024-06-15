package com.cineplanetfactory.retocp.application.port.input;

import com.cineplanetfactory.retocp.domain.dto.ProductDTO;
import com.cineplanetfactory.retocp.domain.request.ProductSaveReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface IProductService {
    ProductDTO findProductById(Long id);
    ProductDTO saveProduct(ProductSaveReq req);
    ProductDTO updateProduct(ProductSaveReq req);
    void deleteProduct(Long id);
    List<ProductDTO> ListAllProducts();
    Page<ProductDTO> findProductPage(Pageable pageable);
    List<ProductDTO> findProductByName(String name);
    List<ProductDTO> findProductByPrice(BigDecimal min, BigDecimal max);
}
