package com.sunset_cafe.sunset_cafe_backend.Service;

import com.sunset_cafe.sunset_cafe_backend.DTO.UserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {
    ResponseEntity<String> signUp(Map<String,String> requestMap);
    ResponseEntity<String> login(Map<String, String> requestMap);
    ResponseEntity<List<UserDTO>> getAllUsers();
    ResponseEntity<String> updateUser(Map<String, String> requestMap);
    ResponseEntity<String> checkToken();
    ResponseEntity<String> changePassword(Map<String, String> requestMap);
}
