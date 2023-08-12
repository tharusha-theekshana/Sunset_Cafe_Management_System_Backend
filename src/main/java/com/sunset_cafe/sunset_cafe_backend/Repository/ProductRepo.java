package com.sunset_cafe.sunset_cafe_backend.Repository;

import com.sunset_cafe.sunset_cafe_backend.DTO.ProductDTO;
import com.sunset_cafe.sunset_cafe_backend.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {

    List<ProductDTO> getAllproducts();

}
