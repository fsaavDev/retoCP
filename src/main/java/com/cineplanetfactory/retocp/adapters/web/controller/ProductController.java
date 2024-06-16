package com.cineplanetfactory.retocp.adapters.web.controller;

import com.cineplanetfactory.retocp.application.port.input.IProductService;
import com.cineplanetfactory.retocp.domain.dto.ProductDTO;
import com.cineplanetfactory.retocp.domain.request.ProductSaveReq;
import com.cineplanetfactory.retocp.domain.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> findProductById(@PathVariable("id") Long id){
        ApiResponse<ProductDTO> apiResponse = new ApiResponse<>(productService.findProductById(id));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> saveProduct(@Valid @RequestBody ProductSaveReq req){
        ApiResponse<ProductDTO> apiResponse = new ApiResponse<>(productService.saveProduct(req));
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(@Valid @RequestBody ProductSaveReq req, @PathVariable("id") Long id){
        ApiResponse<ProductDTO> apiResponse = new ApiResponse<>(productService.updateProduct(req,id));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable("id") Long id){
        productService.deleteProduct(id);
        ApiResponse<String> apiResponse = new ApiResponse<>("Producto eliminado");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<ProductDTO>>> ListAllProducts(){
        ApiResponse<List<ProductDTO>> apiResponse = new ApiResponse<>(productService.ListAllProducts());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/pagination")
    public ResponseEntity<Page<ProductDTO>> findProductPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("code"));
        Page<ProductDTO> pageResponse = productService.findProductPage(pageRequest);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> findProductByName(@PathVariable("name") String name){
        ApiResponse<List<ProductDTO>> apiResponse = new ApiResponse<>(productService.findProductByName(name));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/price")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> findProductByPrice(
            @RequestParam(name = "min",required = true) BigDecimal min,
            @RequestParam(name = "max",required = true) BigDecimal max){
        ApiResponse<List<ProductDTO>> apiResponse = new ApiResponse<>(productService.findProductByPrice(min,max));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/order/price")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> OrderAllProductsByPrice(){
        ApiResponse<List<ProductDTO>> apiResponse = new ApiResponse<>(productService.OrderAllProductsByPrice());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
