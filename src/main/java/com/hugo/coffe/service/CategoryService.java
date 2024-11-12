package com.hugo.coffe.service;

import com.hugo.coffe.model.Category;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    ResponseEntity<String> addNewCategory(Map<String,String> requestMap);

    ResponseEntity<List<Category>> findAll(String filterValue);

    ResponseEntity<String> update(Map<String,String> requestMap);

}
