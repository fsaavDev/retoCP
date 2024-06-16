package com.cineplanetfactory.retocp.adapters.web.controller;

import com.cineplanetfactory.retocp.application.port.input.IOrderService;
import com.cineplanetfactory.retocp.domain.dto.OrderDTO;
import com.cineplanetfactory.retocp.domain.dto.OrderDTO;
import com.cineplanetfactory.retocp.domain.dto.OrderDTO;
import com.cineplanetfactory.retocp.domain.request.OrderSaveReq;
import com.cineplanetfactory.retocp.domain.response.ApiResponse;
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
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDTO>> findOrderById(@PathVariable("id") Long id){
        ApiResponse<OrderDTO> apiResponse = new ApiResponse<>(orderService.findOrderById(id));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDTO>> saveOrder(@Valid @RequestBody OrderSaveReq req){
        ApiResponse<OrderDTO> apiResponse = new ApiResponse<>(orderService.saveOrder(req));
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDTO>> updateOrder(@Valid @RequestBody OrderSaveReq req, @PathVariable("id") Long id){
        ApiResponse<OrderDTO> apiResponse = new ApiResponse<>(orderService.updateOrder(req,id));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteOrder(@PathVariable("id") Long id){
        orderService.deleteOrder(id);
        ApiResponse<String> apiResponse = new ApiResponse<>("Pedido eliminado");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<OrderDTO>>> ListAllOrders(){
        ApiResponse<List<OrderDTO>> apiResponse = new ApiResponse<>(orderService.listAllOrders());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/pagination")
    public ResponseEntity<Page<OrderDTO>> findOrderPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("orderNumber"));
        Page<OrderDTO> pageResponse = orderService.findOrderPage(pageRequest);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/order/quantity")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> sortAllOrdersByQuantity(){
        ApiResponse<List<OrderDTO>> apiResponse = new ApiResponse<>(orderService.sortAllOrdersByQuantity());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/product-name/{name}")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getOrderByOrderName(@PathVariable("name") String name){
        ApiResponse<List<OrderDTO>> apiResponse = new ApiResponse<>(orderService.getOrderByProductName(name));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/customer-lastname/{lastname}")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getOrderByCustomerLastname(@PathVariable("lastname") String lastname){
        ApiResponse<List<OrderDTO>> apiResponse = new ApiResponse<>(orderService.getOrderByCustomerLastname(lastname));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
