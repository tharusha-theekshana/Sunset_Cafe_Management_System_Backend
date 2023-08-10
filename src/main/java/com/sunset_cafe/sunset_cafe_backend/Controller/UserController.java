package com.sunset_cafe.sunset_cafe_backend.Controller;

import com.sunset_cafe.sunset_cafe_backend.DTO.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/user")
public interface UserController {

    @PostMapping(path = "/signUp")
    ResponseEntity<String> signUp(@RequestBody Map<String,String> requestMap);
    @PostMapping(path = "/login")
    ResponseEntity<String> login(@RequestBody Map<String,String> requestMap);
    @GetMapping("/getAllUsers")
    ResponseEntity<List<UserDTO>> getAllUsers();


}
