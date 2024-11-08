package com.hugo.coffe.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/user")
public interface UserRest {

    @PostMapping("/singup")
    public ResponseEntity<String> singUp(@RequestBody(required = true)Map<String,String> requestMap);
}
