package com.hugo.coffe.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {

    ResponseEntity<String> singUp(Map<String, String> requestMap);

    //ResponseEntity<String> login(Map<String, String> requestMap);
}
