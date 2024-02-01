package com.inn.cafe.rest;

import com.inn.cafe.bean.ProductBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/product")
public interface ProductRest {

    @PostMapping(path = "/addProduct")
    ResponseEntity<String> addProduct(@RequestBody(required = true)Map<String,String> requestMap);

    @GetMapping(path = "/getAllProduct")
    ResponseEntity<List<ProductBean>> getAllProduct();

    @PutMapping(path = "/updateProduct")
    ResponseEntity<String> updateProduct(@RequestBody Map<String,String> requestMap);

    @DeleteMapping(path = "/deleteProduct/{id}")
    ResponseEntity<String> deleteProduct(@PathVariable Integer id);

    @PutMapping(path = "/updateStatus")
    ResponseEntity<String> updateStatus(@RequestBody Map<String, String> requestMap);

    @GetMapping(path = "getProductByCategory/{id}")
    ResponseEntity<List<ProductBean>> getByCategory(@PathVariable Integer id);

    @GetMapping(path = "getById/{id}")
    ResponseEntity<ProductBean> getProductById(@PathVariable Integer id);
}
