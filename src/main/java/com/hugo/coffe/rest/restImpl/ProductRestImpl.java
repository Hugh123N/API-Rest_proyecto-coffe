package com.hugo.coffe.rest.restImpl;

import com.hugo.coffe.constens.CoffeConstans;
import com.hugo.coffe.rest.ProductRest;
import com.hugo.coffe.service.ProductService;
import com.hugo.coffe.utils.CoffeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ProductRestImpl implements ProductRest {

    @Autowired
    ProductService productService;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try {
            return productService.addNewProduct(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CoffeUtils.getResponseEntity(CoffeConstans.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
