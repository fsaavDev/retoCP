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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
        Order entity = orderRepository.findById(id)
                .orElseThrow(()-> new ModelNotFoundException("Pedido no encontrado"));
        return mapper.toDTO(entity);
    }

    /**
     * @param req request para crear pedido
     * @return DTO de Pedido
     */
    @Override
    public OrderDTO saveOrder(OrderSaveReq req) {
        Product productEntity = productRepository.findById(req.getProductCode())
                .orElseThrow(()-> new ModelNotFoundException("Producto no encontrado"));
        Order entity = new Order();
        entity.setProduct(productEntity);
        entity.setCustomer(new Order.Customer(req.getNames(),req.getLastnames(),req.getAddress()));
        entity.setQuantity(req.getQuantity());
        return mapper.toDTO(orderRepository.save(entity));
    }

    /**
     * @param req request para actualizar pedido
     * @param id identificador del pedido
     * @return DTO de Pedido
     */
    @Override
    public OrderDTO updateOrder(OrderSaveReq req, Long id) {
        Order entity = orderRepository.findById(id)
                .orElseThrow(()-> new ModelNotFoundException("Pedido no encontrado"));
        entity.setQuantity(req.getQuantity());
        entity.setCustomer(new Order.Customer(req.getNames(),req.getLastnames(),req.getAddress()));
        entity = orderRepository.save(entity);
        return mapper.toDTO(entity);
    }

    /**
     * @param id identificador del pedido a eliminar
     */
    @Override
    public void deleteOrder(Long id) {
        if(!orderRepository.existsById(id)){
            throw new ModelNotFoundException("Pedido no encontrado");
        }
        orderRepository.deleteById(id);
    }

    /**
     * @return Listado de todos los Pedidos sin paginacion
     */
    @Override
    public List<OrderDTO> listAllOrders() {
        return orderRepository.findAll().stream()
                .map(mapper::toDTO) // method reference es menos legible pero mas acorde a programacion funcional
                .collect(Collectors.toList());
    }

    /**
     * @return Listado de todos los Pedidos ordenados por cantidad de productos en el pedido
     */
    @Override
    public List<OrderDTO> sortAllOrdersByQuantity() {
        return orderRepository.findAll(Sort.by("quantity"))
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    /**
     * @param pageable numero y tamaño de pagina a mostrar
     * @return Pagina de pedidos
     */
    @Override
    public Page<OrderDTO> findOrderPage(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(mapper::toDTO);
    }

    /**
     * @param name nombre del producto en el pedido
     * @return Listado de todos los Pedidos que tienen como producto el indicado
     */
    @Override
    public List<OrderDTO> getOrderByProductName(String name) {
        return orderRepository.getByProductName(name).stream()
                .map(mapper::toDTO).collect(Collectors.toList());
    }

    /**
     * @param lastname apellido del cliente del pedido
     * @return Listado de todos los Pedidos que tienen clientes que en su apellido contengan lo indicado
     */
    @Override
    public List<OrderDTO> getOrderByCustomerLastname(String lastname) {
        return orderRepository.getByCustomerLastname(lastname).stream()
                .map(mapper::toDTO).collect(Collectors.toList());
    }
}
