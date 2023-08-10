package com.sunset_cafe.sunset_cafe_backend.JWT;


import com.sunset_cafe.sunset_cafe_backend.Repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomerUserDetailsService implements UserDetailsService{

    private final UserRepo userRepo;
    private com.sunset_cafe.sunset_cafe_backend.Entity.User userDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside LoadUserByUserName {}",username);
        userDetail = userRepo.findByEmail(username);
        if (!Objects.isNull(userDetail)){
            return new User(userDetail.getEmail(),userDetail.getPassword(),new ArrayList<>());
        }else{
            throw  new UsernameNotFoundException("User Not Found .... !");
        }
    }
    

}
