package com.hugo.coffe.rest;

import com.hugo.coffe.model.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/category")
public interface CategoryRest {

    @PostMapping("/add")
    ResponseEntity<String> addNewCategory(@RequestBody(required = true) Map<String,String> requestMap);

    @GetMapping("/get")
    ResponseEntity<List<Category>> findAll(@RequestParam(required = false) String filterValue);

}
