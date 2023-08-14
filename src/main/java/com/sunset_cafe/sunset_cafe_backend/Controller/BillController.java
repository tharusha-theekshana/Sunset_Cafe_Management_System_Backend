package com.sunset_cafe.sunset_cafe_backend.Controller;

import com.sunset_cafe.sunset_cafe_backend.Entity.Bill;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/bill")
public interface BillController {

    @PostMapping(path = "/generateReport")
    ResponseEntity<String> generateReport(@RequestBody Map<String,Object> requestMap);

    @GetMapping(path = "/getBills")
    ResponseEntity<List<Bill>> getBills();
}
