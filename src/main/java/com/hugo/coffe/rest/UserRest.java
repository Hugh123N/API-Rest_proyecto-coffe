package com.hugo.coffe.rest;

import com.hugo.coffe.wraper.UserWraper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping("/user")
public interface UserRest {

    @PostMapping("/signup")
    public ResponseEntity<String> singUp(@RequestBody(required = true)Map<String,String> requestMap);

    @PostMapping("/login")
    public  ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping("/get")
    public ResponseEntity<List<UserWraper>> getAllUser();

}
