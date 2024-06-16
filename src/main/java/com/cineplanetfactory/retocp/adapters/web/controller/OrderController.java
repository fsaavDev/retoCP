package com.cineplanetfactory.retocp.adapters.web.controller;

import com.cineplanetfactory.retocp.application.port.input.IOrderService;
import com.cineplanetfactory.retocp.domain.dto.OrderDTO;
import com.cineplanetfactory.retocp.domain.request.OrderSaveReq;
import com.cineplanetfactory.retocp.domain.response.RetoCpApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
@Tag(name = "Pedidos", description = "Gestion de pedidos")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "pedido encontrado")
    })
    public ResponseEntity<RetoCpApiResponse<OrderDTO>> findOrderById(@PathVariable("id") Long id){
        RetoCpApiResponse<OrderDTO> retoCpApiResponse = new RetoCpApiResponse<>(orderService.findOrderById(id));
        return new ResponseEntity<>(retoCpApiResponse, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "pedido creado")
    })
    public ResponseEntity<RetoCpApiResponse<OrderDTO>> saveOrder(@Valid @RequestBody OrderSaveReq req){
        RetoCpApiResponse<OrderDTO> retoCpApiResponse = new RetoCpApiResponse<>(orderService.saveOrder(req));
        return new ResponseEntity<>(retoCpApiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un pedido por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "pedido creado")
    })
    public ResponseEntity<RetoCpApiResponse<OrderDTO>> updateOrder(@Valid @RequestBody OrderSaveReq req, @PathVariable("id") Long id){
        RetoCpApiResponse<OrderDTO> retoCpApiResponse = new RetoCpApiResponse<>(orderService.updateOrder(req,id));
        return new ResponseEntity<>(retoCpApiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un pedido por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "pedido eliminado"),
    })
    public ResponseEntity<RetoCpApiResponse<String>> deleteOrder(@PathVariable("id") Long id){
        orderService.deleteOrder(id);
        RetoCpApiResponse<String> retoCpApiResponse = new RetoCpApiResponse<>("Pedido eliminado");
        return new ResponseEntity<>(retoCpApiResponse, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Listar todos los pedidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "listado de pedidos encontrados"),
    })
    public ResponseEntity<RetoCpApiResponse<List<OrderDTO>>> ListAllOrders(){
        RetoCpApiResponse<List<OrderDTO>> retoCpApiResponse = new RetoCpApiResponse<>(orderService.listAllOrders());
        return new ResponseEntity<>(retoCpApiResponse, HttpStatus.OK);
    }
    @GetMapping("/pagination")
    @Operation(summary = "Listar todos los pedidos en formato de paginacion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "paginado de pedidos encontrados"),
    })
    public ResponseEntity<Page<OrderDTO>> findOrderPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("orderNumber"));
        Page<OrderDTO> pageResponse = orderService.findOrderPage(pageRequest);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/order/quantity")
    @Operation(summary = "Listar todos los pedidos ordenados por cantidad de productos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "listado de pedidos encontrados"),
    })
    public ResponseEntity<RetoCpApiResponse<List<OrderDTO>>> sortAllOrdersByQuantity(){
        RetoCpApiResponse<List<OrderDTO>> retoCpApiResponse = new RetoCpApiResponse<>(orderService.sortAllOrdersByQuantity());
        return new ResponseEntity<>(retoCpApiResponse, HttpStatus.OK);
    }

    @GetMapping("/product-name/{name}")
    @Operation(summary = "Listar todos los pedidos con el producto con nombre que contenga el valor de la variable name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "listado de pedidos encontrados"),
    })
    public ResponseEntity<RetoCpApiResponse<List<OrderDTO>>> getOrderByOrderName(@PathVariable("name") String name){
        RetoCpApiResponse<List<OrderDTO>> retoCpApiResponse = new RetoCpApiResponse<>(orderService.getOrderByProductName(name));
        return new ResponseEntity<>(retoCpApiResponse, HttpStatus.OK);
    }

    @GetMapping("/customer-lastname/{lastname}")
    @Operation(summary = "Listar todos los pedidos con el apellido del cliente ue contenga el valor de la variable name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "listado de pedidos encontrados"),
    })
    public ResponseEntity<RetoCpApiResponse<List<OrderDTO>>> getOrderByCustomerLastname(@PathVariable("lastname") String lastname){
        RetoCpApiResponse<List<OrderDTO>> retoCpApiResponse = new RetoCpApiResponse<>(orderService.getOrderByCustomerLastname(lastname));
        return new ResponseEntity<>(retoCpApiResponse, HttpStatus.OK);
    }
}
