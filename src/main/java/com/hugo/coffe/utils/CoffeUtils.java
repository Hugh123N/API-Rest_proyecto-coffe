package com.hugo.coffe.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CoffeUtils {

    private CoffeUtils(){

    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}", httpStatus);
    }

}
