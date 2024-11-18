package com.hugo.coffe.service;

import com.hugo.coffe.model.Factura;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface FacturaService {

    ResponseEntity<String> generateReport(Map<String, Object> requestMap);

    ResponseEntity<List<Factura>> findAll();

}
