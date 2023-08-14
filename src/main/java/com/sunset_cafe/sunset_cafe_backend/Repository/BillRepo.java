package com.sunset_cafe.sunset_cafe_backend.Repository;

import com.sunset_cafe.sunset_cafe_backend.Entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepo extends JpaRepository<Bill,Integer> {
    @Query(nativeQuery = true,value = "select * from bill")
    List<Bill> getAllBills();

    @Query(nativeQuery = true,value = "select * from bill where createdby=:username order by id desc")
    List<Bill> getBillsByUserName(@Param("username") String username);
}
