package com.civalue_test.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.civalue_test.model.PersonalizedList;

@Service
public class PersonalizedListService extends AnyDocumentService{    

    @Autowired
    public PersonalizedListService(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    public String upsertPersonalizedList(PersonalizedList personalizedList){
        try {
            upsertDocument(personalizedList, PersonalizedList.class);
        } catch (Exception e) {
            return "Error upserting personalized list data:" + e.getMessage();
        }        
        return "";
    }
    
}
