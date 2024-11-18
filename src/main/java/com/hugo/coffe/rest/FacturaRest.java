package com.hugo.coffe.rest;

import com.hugo.coffe.model.Factura;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/factura")
public interface FacturaRest {

    @PostMapping("/generateReport")
    ResponseEntity<String> generateReport(@RequestBody Map<String, Object> requestMap);

    @GetMapping("getFactura")
    ResponseEntity<List<Factura>> findAll();

}
