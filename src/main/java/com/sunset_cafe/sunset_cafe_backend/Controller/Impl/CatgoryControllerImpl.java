package com.sunset_cafe.sunset_cafe_backend.Controller.Impl;

import com.sunset_cafe.sunset_cafe_backend.Constants.CafeConstants;
import com.sunset_cafe.sunset_cafe_backend.Controller.CategoryController;
import com.sunset_cafe.sunset_cafe_backend.Entity.Category;
import com.sunset_cafe.sunset_cafe_backend.Service.CategoryService;
import com.sunset_cafe.sunset_cafe_backend.Utility.CafeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CatgoryControllerImpl implements CategoryController {

    private final CategoryService categoryService;
    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
           return categoryService.addNewCategory(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCatrgory(String filterValue) {
        try {
            return categoryService.getAllCategory(filterValue);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            return categoryService.updateCategory(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
