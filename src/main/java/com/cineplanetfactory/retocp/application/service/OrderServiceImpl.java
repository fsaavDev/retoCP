package com.cineplanetfactory.retocp.application.service;

import com.cineplanetfactory.retocp.adapters.repository.IOrderRepository;
import com.cineplanetfactory.retocp.adapters.repository.IProductRepository;
import com.cineplanetfactory.retocp.adapters.util.IMapper;
import com.cineplanetfactory.retocp.adapters.web.exception.ModelNotFoundException;
import com.cineplanetfactory.retocp.application.port.input.IOrderService;
import com.cineplanetfactory.retocp.domain.dto.OrderDTO;
import com.cineplanetfactory.retocp.domain.model.Order;
import com.cineplanetfactory.retocp.domain.model.Product;
import com.cineplanetfactory.retocp.domain.request.OrderSaveReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {

    private final IProductRepository productRepository;
    private final IOrderRepository orderRepository;

    private final IMapper mapper;

    /**
     * Dependency Injection por constructor
     *
     * @param productRepository repositorio de Productos
     * @param orderRepository   repositorio de Pedidos
     * @param mapper componente mapper para transformacion de Entidad a DTO, Request a Entity
     */
    public OrderServiceImpl(IProductRepository productRepository, IOrderRepository orderRepository, IMapper mapper) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    /**
     * @param id identificador del pedido
     * @return DTO de Pedido
     */
    @Override
    public OrderDTO findOrderById(Long id) {
        log.info("Buscando pedido con el ID: {}", id);
        Order entity = orderRepository.findById(id)
                .orElseThrow(()-> {
                    log.warn("Pedido no encontrado con el ID: {}", id);
                    return new ModelNotFoundException("Pedido no encontrado");
                });

        return mapper.toDTO(entity);
    }

    /**
     * @param req request para crear pedido
     * @return DTO de Pedido
     */
    @Override
    public OrderDTO saveOrder(OrderSaveReq req) {
        log.info("Guardando producto con informacion del request: {}", req);

        log.info("Buscando producto con el ID: {}", req.getProductCode());
        Product productEntity = productRepository.findById(req.getProductCode())
                .orElseThrow(()-> {
                    log.warn("Producto no encontrado con el ID: {}", req.getProductCode());
                    return new ModelNotFoundException("Producto no encontrado");
                });

        Order entity = new Order();
        entity.setProduct(productEntity);
        entity.setCustomer(new Order.Customer(req.getNames(),req.getLastnames(),req.getAddress()));
        entity.setQuantity(req.getQuantity());

        OrderDTO dto = mapper.toDTO(orderRepository.save(entity));
        log.info("Pedido guardado exitosamente: {}", entity);

        return dto;
    }

    /**
     * @param req request para actualizar pedido
     * @param id identificador del pedido
     * @return DTO de Pedido
     */
    @Override
    public OrderDTO updateOrder(OrderSaveReq req, Long id) {
        log.info("Buscando pedido con el ID: {}", id);
        Order entity = orderRepository.findById(id)
                .orElseThrow(()-> {
                    log.warn("Pedido no encontrado con el ID: {}", id);
                    return new ModelNotFoundException("Pedido no encontrado");
                });
        entity.setQuantity(req.getQuantity());
        entity.setCustomer(new Order.Customer(req.getNames(),req.getLastnames(),req.getAddress()));
        entity = orderRepository.save(entity);
        log.info("Pedido guardado exitosamente: {}", entity);
        return mapper.toDTO(entity);
    }

    /**
     * @param id identificador del pedido a eliminar
     */
    @Override
    public void deleteOrder(Long id) {
        if(!orderRepository.existsById(id)){
            log.warn("Pedido no encontrado con el ID: {}", id);
            throw new ModelNotFoundException("Pedido no encontrado");
        }
        orderRepository.deleteById(id);
        log.info("Pedido eliminado exitosamente");
    }

    /**
     * @return Listado de todos los Pedidos sin paginacion
     */
    @Override
    public List<OrderDTO> listAllOrders() {
        log.info("Listando todos los pedidos");
        return orderRepository.findAll().stream()
                .map(mapper::toDTO) // method reference es menos legible pero mas acorde a programacion funcional
                .collect(Collectors.toList());
    }

    /**
     * @return Listado de todos los Pedidos ordenados por cantidad de productos en el pedido
     */
    @Override
    public List<OrderDTO> sortAllOrdersByQuantity() {
        log.info("Listando todos los pedidos ordenados por cantidad de producots");
        return orderRepository.findAll(Sort.by("quantity"))
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    /**
     * @param pageable numero y tamaño de pagina a mostrar
     * @return Pagina de pedidos
     */
    @Override
    public Page<OrderDTO> findOrderPage(Pageable pageable) {
        log.info("Listando todos los pedidos en formato de paginacion");
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(mapper::toDTO);
    }

    /**
     * @param name nombre del producto en el pedido
     * @return Listado de todos los Pedidos que tienen como producto el indicado
     */
    @Override
    public List<OrderDTO> getOrderByProductName(String name) {
        log.info("Listando todos los pedidos por nombre de producto: {}",name);
        return orderRepository.getByProductName(name).stream()
                .map(mapper::toDTO).collect(Collectors.toList());
    }

    /**
     * @param lastname apellido del cliente del pedido
     * @return Listado de todos los Pedidos que tienen clientes que en su apellido contengan lo indicado
     */
    @Override
    public List<OrderDTO> getOrderByCustomerLastname(String lastname) {
        log.info("Listando todos los productos por apellido de cliente: {}",lastname);
        return orderRepository.getByCustomerLastname(lastname).stream()
                .map(mapper::toDTO).collect(Collectors.toList());
    }
}
