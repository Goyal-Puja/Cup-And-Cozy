package com.inn.cafe.serviceImpl;

import com.inn.cafe.repository.BillRepository;
import com.inn.cafe.repository.CategoryRepository;
import com.inn.cafe.repository.ProductRepository;
import com.inn.cafe.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    BillRepository billRepository;
    @Autowired
    ProductRepository productRepository;
    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        try {
            Map<String,Object> map = new HashMap<>();
            map.put("category", categoryRepository.count());
            map.put("bill", billRepository.count());
            map.put("product",productRepository.count());
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
