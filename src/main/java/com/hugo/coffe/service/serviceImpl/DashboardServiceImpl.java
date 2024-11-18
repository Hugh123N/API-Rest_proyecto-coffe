package com.hugo.coffe.service.serviceImpl;

import com.hugo.coffe.repository.CategoryRepository;
import com.hugo.coffe.repository.FacturaRepository;
import com.hugo.coffe.repository.ProductRepository;
import com.hugo.coffe.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    FacturaRepository facturaRepository;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String,Object> map=new HashMap<>();
        map.put("category",categoryRepository.count());
        map.put("product",productRepository.count());
        map.put("factura",facturaRepository.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
