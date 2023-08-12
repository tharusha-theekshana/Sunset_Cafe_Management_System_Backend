package com.sunset_cafe.sunset_cafe_backend.Repository;

import com.sunset_cafe.sunset_cafe_backend.DTO.ProductDTO;
import com.sunset_cafe.sunset_cafe_backend.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

    List<ProductDTO> getAllproducts();

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update product set product.status=:status where id=:id")
    Integer updateProductStatus(@Param("status") String status, @Param("id") Integer id);

    List<ProductDTO> getProductsByCategory(@Param("id") Integer id);
}
