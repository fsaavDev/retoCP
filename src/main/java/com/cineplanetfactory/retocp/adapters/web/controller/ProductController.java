package com.cineplanetfactory.retocp.adapters.web.controller;

import com.cineplanetfactory.retocp.application.port.input.IProductService;
import com.cineplanetfactory.retocp.domain.dto.ProductDTO;
import com.cineplanetfactory.retocp.domain.request.ProductSaveReq;
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
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "Gestion de productos")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "producto encontrado")
    })
    public ResponseEntity<RetoCpApiResponse<ProductDTO>> findProductById(@PathVariable("id") Long id){
        RetoCpApiResponse<ProductDTO> retoCpApiResponse = new RetoCpApiResponse<>(productService.findProductById(id));
        return new ResponseEntity<>(retoCpApiResponse, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "producto creado")
    })
    public ResponseEntity<RetoCpApiResponse<ProductDTO>> saveProduct(@Valid @RequestBody ProductSaveReq req){
        RetoCpApiResponse<ProductDTO> retoCpApiResponse = new RetoCpApiResponse<>(productService.saveProduct(req));
        return new ResponseEntity<>(retoCpApiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "producto encontrado")
    })
    public ResponseEntity<RetoCpApiResponse<ProductDTO>> updateProduct(@Valid @RequestBody ProductSaveReq req, @PathVariable("id") Long id){
        RetoCpApiResponse<ProductDTO> retoCpApiResponse = new RetoCpApiResponse<>(productService.updateProduct(req,id));
        return new ResponseEntity<>(retoCpApiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "producto eliminado"),
    })
    public ResponseEntity<RetoCpApiResponse<String>> deleteProduct(@PathVariable("id") Long id){
        productService.deleteProduct(id);
        RetoCpApiResponse<String> retoCpApiResponse = new RetoCpApiResponse<>("Producto eliminado");
        return new ResponseEntity<>(retoCpApiResponse, HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Listar todos los productos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "listado de productos encontrados"),
    })
    public ResponseEntity<RetoCpApiResponse<List<ProductDTO>>> ListAllProducts(){
        RetoCpApiResponse<List<ProductDTO>> retoCpApiResponse = new RetoCpApiResponse<>(productService.ListAllProducts());
        return new ResponseEntity<>(retoCpApiResponse, HttpStatus.OK);
    }
    @GetMapping("/pagination")
    @Operation(summary = "Listar todos los productos en formato de paginacion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "paginado de productos encontrados"),
    })
    public ResponseEntity<Page<ProductDTO>> findProductPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("code"));
        Page<ProductDTO> pageResponse = productService.findProductPage(pageRequest);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Listar todos los productos con nombre que contenga el valor de la variable name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "listado de productos encontrados"),
    })
    public ResponseEntity<RetoCpApiResponse<List<ProductDTO>>> findProductByName(@PathVariable("name") String name){
        RetoCpApiResponse<List<ProductDTO>> retoCpApiResponse = new RetoCpApiResponse<>(productService.findProductByName(name));
        return new ResponseEntity<>(retoCpApiResponse, HttpStatus.OK);
    }
    @GetMapping("/price")
    @Operation(summary = "Listar todos los productos con precios entre los valores de las variables min y max")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "listado de productos encontrados"),
    })
    public ResponseEntity<RetoCpApiResponse<List<ProductDTO>>> findProductByPrice(
            @RequestParam(name = "min",required = true) BigDecimal min,
            @RequestParam(name = "max",required = true) BigDecimal max){
        RetoCpApiResponse<List<ProductDTO>> retoCpApiResponse = new RetoCpApiResponse<>(productService.findProductByPrice(min,max));
        return new ResponseEntity<>(retoCpApiResponse, HttpStatus.OK);
    }

    @GetMapping("/order/price")
    @Operation(summary = "Listar todos los productos ordenados por precio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "listado de productos encontrados"),
    })
    public ResponseEntity<RetoCpApiResponse<List<ProductDTO>>> OrderAllProductsByPrice(){
        RetoCpApiResponse<List<ProductDTO>> retoCpApiResponse = new RetoCpApiResponse<>(productService.OrderAllProductsByPrice());
        return new ResponseEntity<>(retoCpApiResponse, HttpStatus.OK);
    }
}
