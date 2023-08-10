package com.sunset_cafe.sunset_cafe_backend.Repository;


import com.sunset_cafe.sunset_cafe_backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

    @Query(nativeQuery = true,value = "select * from user where user.email=:email")
    User findByEmail(@Param("email")String email);



}
