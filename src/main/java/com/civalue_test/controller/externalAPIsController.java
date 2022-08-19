package com.civalue_test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.civalue_test.service.PersonalizedListService;
import com.civalue_test.service.ProductService;

@RestController
@RequestMapping("/api/ec_v1")
public class externalAPIsController {
    private final ProductService productService;
    private final PersonalizedListService personalizedListService;

    @Autowired
    public externalAPIsController(ProductService productService, PersonalizedListService personalizedListService) {
        this.productService = productService;
        this.personalizedListService = personalizedListService;
    }

    @RequestMapping(value = "/product", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> get_products(
        @RequestParam(required = true, value = "shopperId") String shopperId,
        @RequestParam(required = false, value = "category", defaultValue = "") String category,
        @RequestParam(required = false, value = "brand", defaultValue = "") String brand,
        @RequestParam(required = false, value = "limit", defaultValue = "10") Integer limit
    ) {        
        Object result = productService.getProductsByFilter(shopperId, category, brand, limit);
        if (result instanceof String){
            return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }else if (result instanceof List){
            return ResponseEntity.ok(result);
        }
        return new ResponseEntity<Object>("Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);        
    }

    @RequestMapping(value = "/shopper", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> get_shoppers(
        @RequestParam(required = true, value = "productId") String productId,
        @RequestParam(required = false, value = "limit", defaultValue = "10") Integer limit
    ) {
        Object result = personalizedListService.getShoppersByFilter(productId, limit);
        if (result instanceof String){
            return new ResponseEntity<Object>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }else if (result instanceof List){
            return ResponseEntity.ok(result);
        }
        return new ResponseEntity<Object>("Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);        
    }

}
