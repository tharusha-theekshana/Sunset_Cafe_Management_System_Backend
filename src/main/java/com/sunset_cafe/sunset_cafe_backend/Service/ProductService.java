package com.sunset_cafe.sunset_cafe_backend.Service;

import com.sunset_cafe.sunset_cafe_backend.DTO.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ProductService {
    ResponseEntity<String> addNewProduct(Map<String, String> requestMap);
    ResponseEntity<List<ProductDTO>> getAllProducts();
    ResponseEntity<String> updateProduct(Map<String, String> requestMap);
    ResponseEntity<String> deleteProduct(Integer id);
    ResponseEntity<String> updateStatus(Map<String, String> requestMap);
    ResponseEntity<List<ProductDTO>> getProductsByCategory(Integer id);
}
