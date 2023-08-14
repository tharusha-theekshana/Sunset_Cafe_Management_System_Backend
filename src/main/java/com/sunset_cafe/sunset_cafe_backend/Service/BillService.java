package com.sunset_cafe.sunset_cafe_backend.Service;


import com.sunset_cafe.sunset_cafe_backend.Entity.Bill;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface BillService {
    ResponseEntity<String> generateReport(Map<String, Object> requestMap);
    ResponseEntity<List<Bill>> getBills();
    ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap);
    ResponseEntity<String> deleteBill(Integer id);
}
