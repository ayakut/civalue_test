package com.civalue_test.service;


import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.civalue_test.model.PersonalizedList;

@Service
public class PersonalizedListService extends AnyDocumentService{    

    @Autowired
    public PersonalizedListService(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    public String upsertPersonalizedList(PersonalizedList personalizedList){
        try {
            this.upsertDocument(personalizedList, PersonalizedList.class);
        } catch (Exception e) {
            return "Error upserting personalized list data:" + e.getMessage();
        }        
        return "";
    }

    public Object getShoppersByFilter(String productId, Integer limit){
        //checking critical parameters' value
        if (!StringUtils.hasText(productId)) return "Product ID must not be empty!";
        if (limit < 1 || limit > 100){
            return "The limit value must be between 1 and 100 (including)!";
        }
        /*building aggregation:
          from personalized list collection
          select document where shelf list contains a product with shopperId=<produtId>
          limit results with provided limit value
          project shopperId field only
          return result
        */
        List<AggregationOperation> pipeline = new LinkedList<>();
        //select document where shopperId=<parameter>
        pipeline.add(Aggregation.match(
            new Criteria().and(PersonalizedList.SHELF_FIELD)
                .elemMatch(Criteria.where(PersonalizedList.Personalization.PRODUCT_ID_FIELD).is(productId))));
        //limit results with provided limit value
        pipeline.add(Aggregation.limit(limit));
        //final projection        
        pipeline.add(Aggregation.project(PersonalizedList.ID_FIELD).andExclude("_id"));
        Aggregation agg = Aggregation.newAggregation(pipeline);
        List<Document> results = this.getTemplate().aggregate(agg, PersonalizedList.class, Document.class).getMappedResults();
        return results;
    }
    
}
