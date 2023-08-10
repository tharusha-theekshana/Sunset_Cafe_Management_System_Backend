package com.sunset_cafe.sunset_cafe_backend.Service.Impl;

import com.sunset_cafe.sunset_cafe_backend.Constants.CafeConstants;
import com.sunset_cafe.sunset_cafe_backend.Entity.User;
import com.sunset_cafe.sunset_cafe_backend.Repository.UserRepo;
import com.sunset_cafe.sunset_cafe_backend.Service.UserService;
import com.sunset_cafe.sunset_cafe_backend.Utility.CafeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside SignUp {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userRepo.findByEmail(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userRepo.save(mapUser(requestMap));
                    return CafeUtils.getResponseEntity(CafeConstants.REGISTERED_SUCCESS, HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity(CafeConstants.EMAIL_EXISTS, HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")) {
            return true;
        } else {
            return false;
        }
    }

    private User mapUser(Map<String, String> requestMap) {
        User userObject = new User();
        userObject.setName(requestMap.get("name"));
        userObject.setContactNumber(requestMap.get("contactNumber"));
        userObject.setEmail(requestMap.get("email"));
        userObject.setPassword(requestMap.get("password"));
        userObject.setRole("user");
        userObject.setStatus("false");
        return userObject;
    }
}
