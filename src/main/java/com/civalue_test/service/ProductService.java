package com.civalue_test.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.civalue_test.model.Product;

@Service
public class ProductService extends AnyDocumentService{

    @Autowired
    public ProductService(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    } 

    public String upsertProduct(Product product){
        try {
            upsertDocument(product, Product.class);   
        } catch (Exception e) {
            return "Error upserting product data:" + e.getMessage();
        }        
        return "";
    }
    
}
