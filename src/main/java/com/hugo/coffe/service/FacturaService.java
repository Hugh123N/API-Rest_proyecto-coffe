package com.hugo.coffe.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Objects;

public interface FacturaService {

    ResponseEntity<String> generateReport(Map<String, Object> requestMap);

}
