package com.sunset_cafe.sunset_cafe_backend.Service.Impl;

import com.google.common.base.Strings;
import com.sunset_cafe.sunset_cafe_backend.Constants.CafeConstants;
import com.sunset_cafe.sunset_cafe_backend.DTO.UserDTO;
import com.sunset_cafe.sunset_cafe_backend.Entity.User;
import com.sunset_cafe.sunset_cafe_backend.JWT.CustomerUserDetailsService;
import com.sunset_cafe.sunset_cafe_backend.JWT.JwtFilter;
import com.sunset_cafe.sunset_cafe_backend.JWT.JwtUtil;
import com.sunset_cafe.sunset_cafe_backend.Repository.UserRepo;
import com.sunset_cafe.sunset_cafe_backend.Service.UserService;
import com.sunset_cafe.sunset_cafe_backend.Utility.CafeUtils;
import com.sunset_cafe.sunset_cafe_backend.Utility.EmailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final CustomerUserDetailsService customerUserDetailsService;
    private final JwtUtil jwtUtil;
    private final JwtFilter jwtFilter;
    private final EmailUtils emailUtils;

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

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside Login {}", requestMap);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if (authentication.isAuthenticated()) {
                if (customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<String>("{\"token\":\"" + jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
                            customerUserDetailsService.getUserDetail().getRole()) + "\"}", HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>(CafeConstants.WAIT_FOR_ADMIN_APPROVAL, HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.BAD_CREDENTIALS, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            if (jwtFilter.isAdmin()) {
                return new ResponseEntity<>(userRepo.getAllUsers(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<List<UserDTO>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateUser(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<User> optional = userRepo.findById(Integer.valueOf(requestMap.get("id")));
                if (!optional.isEmpty()){
                    userRepo.updateStatus(requestMap.get("status"), Integer.valueOf(requestMap.get("id")));
                    sendMailToAllAdmins(requestMap.get("status"),optional.get().getEmail(),userRepo.getAllAdmins());
                    return CafeUtils.getResponseEntity(CafeConstants.USER_UPDATE_SUCCESSFULLY,HttpStatus.OK);
                }else{
                    return CafeUtils.getResponseEntity(CafeConstants.USER_DOESNT_EXISTS, HttpStatus.NOT_FOUND);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailToAllAdmins(String status, String userEmail, List<String> allAdmins) {
        allAdmins.remove(jwtFilter.getCurrentUser());
        if (status != null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),CafeConstants.ACCOUNT_APPROVED,"USER:- "+ userEmail +" \n is approved by \n ADMIN :-" + jwtFilter.getCurrentUser(),allAdmins);
        }else{
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),CafeConstants.ACCOUNT_DISABLED,"USER:- "+ userEmail +" \n is disabled by \n ADMIN :-" + jwtFilter.getCurrentUser(),allAdmins);
        }
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return CafeUtils.getResponseEntity("True", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObj = userRepo.findByEmail(jwtFilter.getCurrentUser());
            if (!userObj.equals(null)){
                if (userObj.getPassword().equals(requestMap.get("oldPassword"))){
                    userObj.setPassword(requestMap.get("newPassword"));
                    userRepo.save(userObj);
                    return CafeUtils.getResponseEntity(CafeConstants.PASSWORD_UPDATED_SUCCESSFULLY,HttpStatus.OK);
                }else{
                    return CafeUtils.getResponseEntity(CafeConstants.INCORRECT_OLD_PASSWORD,HttpStatus.BAD_REQUEST);
                }
            }else{
                return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userRepo.findByEmail(requestMap.get("email"));
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())){
                emailUtils.forgotMail(user.getEmail(),CafeConstants.CREDENTIALS_BY_SUNSET_CAFE,user.getPassword());
                return CafeUtils.getResponseEntity(CafeConstants.CHECK_YOUR_MAIL_FOR_CREDENTIALS,HttpStatus.OK);
            }else{
                return CafeUtils.getResponseEntity(CafeConstants.EMAIL_NOT_FOUND,HttpStatus.NOT_FOUND);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
