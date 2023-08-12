package com.sunset_cafe.sunset_cafe_backend.Controller;

import com.sunset_cafe.sunset_cafe_backend.Entity.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/category")
public interface CategoryController {

    @PostMapping(path = "/add")
    ResponseEntity<String> addNewCategory(@RequestBody Map<String,String> requestMap);

    @GetMapping(path = "/get")
    ResponseEntity<List<Category>> getAllCatrgory(@RequestParam(required = false) String filterValue);

    @PostMapping(path = "/update")
    ResponseEntity<String> updateCategory(@RequestBody Map<String,String> requestMap);

}
