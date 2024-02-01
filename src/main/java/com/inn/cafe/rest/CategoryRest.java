package com.inn.cafe.rest;

import com.inn.cafe.model.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/category")
public interface CategoryRest {

    @PostMapping(path = "/addCategory")
    ResponseEntity<String> addCategory(@RequestBody(required = true)Map<String,String> requestMap);

    @GetMapping(path = "/getAllCategory")
    ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false) String filterValue);

    @PutMapping(path = "/updateCategory")   // not working
    ResponseEntity<String> updateCategory(@RequestParam(required = true) Map<String, String> requestMap);
}
