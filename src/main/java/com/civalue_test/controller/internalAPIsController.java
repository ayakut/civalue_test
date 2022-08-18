package com.civalue_test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.civalue_test.model.PersonalizedList;
import com.civalue_test.model.Product;
import com.civalue_test.service.PersonalizedListService;
import com.civalue_test.service.ProductService;

@RestController
@RequestMapping("/api/i_v1")
public class internalAPIsController {
    private final ProductService productService;
    private final PersonalizedListService personalizedListService;

    @Autowired
    public internalAPIsController(ProductService productService, PersonalizedListService personalizedListService) {
        this.productService = productService;
        this.personalizedListService = personalizedListService;
    }

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public ResponseEntity<String> upsert_product(@RequestBody Product product) {
        String err = productService.upsertProduct(product);
        if (err.length() == 0) return ResponseEntity.ok("ok");
        return new ResponseEntity<String>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/personalized_list", method = RequestMethod.POST)
    public ResponseEntity<String> upsert_product(@RequestBody PersonalizedList personalizedList) {
        String err = personalizedListService.upsertPersonalizedList(personalizedList);
        if (err.length() == 0) return ResponseEntity.ok("ok");
        return new ResponseEntity<String>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
