package com.sunset_cafe.sunset_cafe_backend.Service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {
    ResponseEntity<String> signUp(Map<String,String> requestMap);

}
