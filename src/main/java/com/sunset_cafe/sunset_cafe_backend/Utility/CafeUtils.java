package com.sunset_cafe.sunset_cafe_backend.Utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CafeUtils {
    public CafeUtils() {
    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\" : \""+responseMessage+"\"}",httpStatus);
    }
}
