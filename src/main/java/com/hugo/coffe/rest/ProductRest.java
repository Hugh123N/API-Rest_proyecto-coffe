package com.hugo.coffe.rest;

import com.hugo.coffe.model.Product;
import com.hugo.coffe.wraper.ProductWraper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/product")
public interface ProductRest {

    @PostMapping("/add")
    ResponseEntity<String> addNewProduct(@RequestBody Map<String,String> requestMap);

    @GetMapping("get")
    ResponseEntity<List<ProductWraper>> getAllProduct();

    @PostMapping("/update")
    ResponseEntity<String> update(@RequestBody Map<String,String> requestMap);

    @PostMapping("delete/{id}")
    ResponseEntity<String> delete(@PathVariable Integer id);

}
