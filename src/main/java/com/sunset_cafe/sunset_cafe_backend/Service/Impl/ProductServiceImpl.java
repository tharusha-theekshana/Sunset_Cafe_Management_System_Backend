package com.sunset_cafe.sunset_cafe_backend.Service.Impl;

import com.sunset_cafe.sunset_cafe_backend.Constants.CafeConstants;
import com.sunset_cafe.sunset_cafe_backend.DTO.ProductDTO;
import com.sunset_cafe.sunset_cafe_backend.Entity.Category;
import com.sunset_cafe.sunset_cafe_backend.Entity.Product;
import com.sunset_cafe.sunset_cafe_backend.JWT.JwtFilter;
import com.sunset_cafe.sunset_cafe_backend.Repository.ProductRepo;
import com.sunset_cafe.sunset_cafe_backend.Service.ProductService;
import com.sunset_cafe.sunset_cafe_backend.Utility.CafeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateProductMap(requestMap, false)) {
                    productRepo.save(getProductFromMap(requestMap, false));
                    return CafeUtils.getResponseEntity(CafeConstants.PRODUCT_ADDED_SUCCESSFULLY, HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    private Product getProductFromMap(Map<String, String> requestMap, Boolean isAdd) {
        Category category = new Category();
        category.setId(Integer.valueOf(requestMap.get("categoryId")));

        Product product = new Product();
        if (isAdd) {
            product.setId(Integer.valueOf(requestMap.get("id")));
        } else {
            product.setStatus("true");
        }
        product.setName(requestMap.get("name"));
        product.setCategory(category);
        product.setDescription(requestMap.get("description"));
        product.setPrice(Integer.valueOf(requestMap.get("price")));
        return product;
    }

    @Override
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        try {
            return new ResponseEntity<List<ProductDTO>>(productRepo.getAllproducts(), HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateProductMap(requestMap, true)) {
                    Optional<Product> optional = productRepo.findById(Integer.valueOf(requestMap.get("id")));
                    if (!optional.isEmpty()) {
                        Product product = getProductFromMap(requestMap, true);
                        product.setStatus(optional.get().getStatus());
                        productRepo.save(product);
                        return CafeUtils.getResponseEntity(CafeConstants.PRODUCT_UPDATE_SUCCESSFULLY, HttpStatus.OK);
                    } else {
                        return CafeUtils.getResponseEntity(CafeConstants.PRODUCT_ID_DOSENT_EXISTS, HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<Product> optional = productRepo.findById(id);
                if (!optional.isEmpty()) {
                    productRepo.deleteById(id);
                    return CafeUtils.getResponseEntity(CafeConstants.PRODUCT_DELETE_SUCCESSFULLY, HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity(CafeConstants.PRODUCT_ID_DOSENT_EXISTS, HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<Product> optional = productRepo.findById(Integer.valueOf(requestMap.get("id")));
                if (!optional.isEmpty()){
                    productRepo.updateProductStatus(requestMap.get("status"),Integer.valueOf(requestMap.get("id")));
                    return CafeUtils.getResponseEntity(CafeConstants.PRODUCT_STATUS_UPDATE_SUCCESSFULLY, HttpStatus.OK);
                }else{
                    return CafeUtils.getResponseEntity(CafeConstants.PRODUCT_ID_DOSENT_EXISTS, HttpStatus.NOT_FOUND);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(Integer id) {
        try {
            return new ResponseEntity<List<ProductDTO>>(productRepo.getProductsByCategory(id), HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
