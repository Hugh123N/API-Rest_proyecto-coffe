package com.hugo.coffe.service;

import com.hugo.coffe.wraper.ProductWraper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ProductService {

    ResponseEntity<String> addNewProduct(Map<String,String> requestMap);

    ResponseEntity<List<ProductWraper>> getAllProduct();

}
