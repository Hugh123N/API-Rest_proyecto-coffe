package com.hugo.coffe.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ProductService {

    ResponseEntity<String> addNewProduct(Map<String,String> requestMap);

}
