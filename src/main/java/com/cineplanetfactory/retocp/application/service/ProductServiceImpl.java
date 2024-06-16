package com.cineplanetfactory.retocp.application.service;

import com.cineplanetfactory.retocp.adapters.repository.IProductRepository;
import com.cineplanetfactory.retocp.adapters.util.IMapper;
import com.cineplanetfactory.retocp.adapters.web.exception.ModelNotFoundException;
import com.cineplanetfactory.retocp.application.port.input.IProductService;
import com.cineplanetfactory.retocp.domain.dto.ProductDTO;
import com.cineplanetfactory.retocp.domain.model.Product;
import com.cineplanetfactory.retocp.domain.request.ProductSaveReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired // Dependency injection by annotation
    private IProductRepository productRepository;

    @Autowired
    private IMapper mapper;


    /**
     * @param id identificador del Producto
     * @return DTO de Producto
     */
    @Override
    public ProductDTO findProductById(Long id) {
        Product entity = productRepository.findById(id).orElseThrow(()-> new ModelNotFoundException("Producto no encontrado"));
        return mapper.toDTO(entity);
    }

    /**
     * @param req request para crear producto
     * @return DTO de Producto
     */
    @Override
    public ProductDTO saveProduct(ProductSaveReq req) {
        Product entity = productRepository.save(mapper.toEntity(req));
        return mapper.toDTO(entity);
    }

    /**
     * @param req request para actualizar producto
     * @param id identificador de producto a actualizar
     * @return DTO de Producto
     */
    @Override
    public ProductDTO updateProduct(ProductSaveReq req, Long id) {
        Product entity = productRepository.findById(id).orElseThrow(()-> new ModelNotFoundException("Producto no encontrado"));
        entity.setName(req.getName());
        entity.setPrice(req.getPrice());
        entity = productRepository.save(entity);
        return mapper.toDTO(entity);
    }

    /**
     * @param id identificador de producto a eliminar
     */
    @Override
    public void deleteProduct(Long id) {
        if(!productRepository.existsById(id)){
            throw new ModelNotFoundException("Producto no encontrado");
        }
        productRepository.deleteById(id);
    }

    /**
     * @return Listado de todos los Productos sin paginacion
     */
    @Override
    public List<ProductDTO> ListAllProducts() {
        return productRepository.findAll().stream()
                .map(p->mapper.toDTO(p)).collect(Collectors.toList());
    }

    /**
     * @return Listado de todos los Productos ordenados por precio
     */
    @Override
    public List<ProductDTO> OrderAllProductsByPrice() {
        return productRepository.findAll(Sort.by("price")).stream()
                .map(p->mapper.toDTO(p)).collect(Collectors.toList());
    }

    /**
     * @param pageable numero y tamaño de pagina a mostrar
     * @return Pagina de productos
     */
    @Override
    public Page<ProductDTO> findProductPage(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(p->mapper.toDTO(p));
    }

    /**
     * @param name nombre del producto
     * @return listado de productos que contengan el valor de name en su nombre
     */
    @Override
    public List<ProductDTO> findProductByName(String name) {
        return productRepository.findByNameContains(name).stream()
                .map(p->mapper.toDTO(p)).collect(Collectors.toList());
    }

    /**
     * @param min precio minimo
     * @param max precio maximo
     * @return listado de productos con precio entre min y max
     */
    @Override
    public List<ProductDTO> findProductByPrice(BigDecimal min, BigDecimal max) {
        return productRepository.findByPriceBetween(min,max).stream()
                .map(p->mapper.toDTO(p)).collect(Collectors.toList());
    }
}
