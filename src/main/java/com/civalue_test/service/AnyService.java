package com.civalue_test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public abstract class AnyService {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public AnyService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public MongoTemplate getTemplate(){
        return this.mongoTemplate;
    }

}
