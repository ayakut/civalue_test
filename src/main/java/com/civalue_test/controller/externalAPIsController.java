package com.civalue_test.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ec_v1")
public class externalAPIsController {

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public ResponseEntity<String> get_products() {
        return new ResponseEntity<String>("Not implemented yet", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/shopper", method = RequestMethod.GET)
    public ResponseEntity<String> get_shoppers() {
        return new ResponseEntity<String>("Not implemented yet", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
