package com.sunset_cafe.sunset_cafe_backend.Repository;

import com.sunset_cafe.sunset_cafe_backend.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Integer> {

    @Query(nativeQuery = true, value = "select * from category")
    List<Category> getAllCategory();


}
