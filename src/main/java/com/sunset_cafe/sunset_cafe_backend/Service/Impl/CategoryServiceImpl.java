package com.sunset_cafe.sunset_cafe_backend.Service.Impl;

import com.sunset_cafe.sunset_cafe_backend.Constants.CafeConstants;
import com.sunset_cafe.sunset_cafe_backend.Entity.Category;
import com.sunset_cafe.sunset_cafe_backend.JWT.JwtFilter;
import com.sunset_cafe.sunset_cafe_backend.Repository.CategoryRepo;
import com.sunset_cafe.sunset_cafe_backend.Service.CategoryService;
import com.sunset_cafe.sunset_cafe_backend.Utility.CafeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    private final JwtFilter jwtFilter;
    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
             if (jwtFilter.isAdmin()){
                 if (validateCategoryMap(requestMap,false)){
                    categoryRepo.save(getCategoryFromMap(requestMap,false));
                     return CafeUtils.getResponseEntity(CafeConstants.CATEGORY_ADD_SUCCESSFULLY,HttpStatus.OK);
                 }
             }else{
                 return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
             }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")){
            if (requestMap.containsKey("id") && validateId){
                return true;
            }else if(!validateId){
                return true;
            }
        }
        return false;
    }

    private Category getCategoryFromMap(Map<String, String> requestMap,Boolean isAdd){
        Category category = new Category();
        if (isAdd){
            category.setId(Integer.valueOf(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }
}
