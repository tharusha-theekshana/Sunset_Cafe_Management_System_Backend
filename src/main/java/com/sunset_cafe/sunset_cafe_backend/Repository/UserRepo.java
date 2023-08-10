package com.sunset_cafe.sunset_cafe_backend.Repository;


import com.sunset_cafe.sunset_cafe_backend.DTO.UserDTO;
import com.sunset_cafe.sunset_cafe_backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

    @Query(nativeQuery = true,value = "select * from user where user.email=:email")
    User findByEmail(@Param("email")String email);

    List<UserDTO> getAllUsers();

    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "update user set user.status=:status where user.id=:id")
    Integer updateStatus(@Param("status")String status,@Param("id")Integer id);

    @Query(nativeQuery = true,value = "select user.email from user where user.role='admin'")
    List<String> getAllAdmins();


}
