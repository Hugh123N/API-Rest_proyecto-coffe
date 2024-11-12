package com.hugo.coffe.rest.restImpl;

import com.hugo.coffe.constens.CoffeConstans;
import com.hugo.coffe.model.Category;
import com.hugo.coffe.rest.CategoryRest;
import com.hugo.coffe.service.CategoryService;
import com.hugo.coffe.utils.CoffeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryRestImpl implements CategoryRest {

    @Autowired
    CategoryService categoryService;


    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            return categoryService.addNewCategory(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CoffeUtils.getResponseEntity(CoffeConstans.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> findAll(String filterValue) {
        try {
            return categoryService.findAll(filterValue);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
