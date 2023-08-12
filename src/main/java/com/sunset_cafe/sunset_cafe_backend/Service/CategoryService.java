package com.sunset_cafe.sunset_cafe_backend.Service;

import com.sunset_cafe.sunset_cafe_backend.Entity.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    ResponseEntity<String> addNewCategory(Map<String, String> requestMap);
    ResponseEntity<List<Category>> getAllCategory(String filterValue);
    ResponseEntity<String> updateCategory(Map<String, String> requestMap);
}
