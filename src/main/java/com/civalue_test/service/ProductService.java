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
import com.civalue_test.model.Product;

@Service
public class ProductService extends AnyDocumentService{

    @Autowired
    public ProductService(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    } 

    public String upsertProduct(Product product){
        try {
            this.upsertDocument(product, Product.class);   
        } catch (Exception e) {
            return "Error upserting product data:" + e.getMessage();
        }        
        return "";
    }

    public Object getProductsByFilter(String shopperId, String category, String brand, Integer limit){
        //checking critical parameters' value
        if (!StringUtils.hasText(shopperId)) return "Shopper ID must not be empty!";
        if (limit < 1 || limit > 100){
            return "The limit value must be between 1 and 100 (including)!";
        }
        /*building aggregation:
          from personalized list collection
          select document where shopperId=<parameter>
          unwind shelf list to split one document into a list of documents          
          group the list by product id to avoid duplicats          
          look up in the product collection to join product data
          unwind by lookup results
          filtering product data by category, brand value if not empty values are provided in parameters
          limit results with provided limit value
          return result
        */
        List<AggregationOperation> pipeline = new LinkedList<>();
        //select document where shopperId=<parameter>
        pipeline.add(Aggregation.match(Criteria.where(PersonalizedList.ID_FIELD).is(shopperId)));
        //unwind shelf list to split one document into a list of documents          
        pipeline.add(Aggregation.unwind(PersonalizedList.SHELF_FIELD));
        //group the list by product id to avoid duplicats          
        pipeline.add(Aggregation.group(PersonalizedList.SHELF_FIELD+"."+Product.ID_FIELD));
        //look up in the product collection to join product data
        String joinedProductFieldName = "productJoined";
        Document lookupOperation = new Document("from", Product.COLLECTION_NAME)
            .append("localField", "_id")
            .append("foreignField", Product.ID_FIELD)
            .append("as", joinedProductFieldName);
        AggregationOperation lookupStage = context -> context.getMappedObject(new Document("$lookup", lookupOperation));
        pipeline.add(lookupStage);
        //unwind by lookup results
        Document unwindOperation = new Document("path", "$"+joinedProductFieldName);
        AggregationOperation unwindStage = context -> context.getMappedObject(new Document("$unwind", unwindOperation));
        pipeline.add(unwindStage);
        //filtering product data by category, brand value if not empty values are provided in parameters
        if(StringUtils.hasText(category) || StringUtils.hasText(brand)){
            Criteria[] criteria = new Criteria[2];
            int criteriaIndex = 0;
            if (StringUtils.hasText(category)) {
                criteria[criteriaIndex++] = Criteria.where(joinedProductFieldName+"."+Product.CATEGORY_FIELD).is(category);
            }
            if (StringUtils.hasText(brand)) {
                criteria[criteriaIndex++] = Criteria.where(joinedProductFieldName+"."+Product.BRAND_FIELD).is(brand);
            }
            if (criteriaIndex == 1){
                pipeline.add(Aggregation.match(criteria[0]));
            }else{
                pipeline.add(Aggregation.match(new Criteria().andOperator(criteria)));
            }            
        }
        //limit results with provided limit value
        pipeline.add(Aggregation.limit(limit));
        //final projection
        Document replaceRootOperation = new Document("newRoot", "$"+joinedProductFieldName);
        AggregationOperation replaceRootStage = context -> context.getMappedObject(new Document("$replaceRoot", replaceRootOperation));
        pipeline.add(replaceRootStage);
        Aggregation agg = Aggregation.newAggregation(pipeline);
        List<Document> results = this.getTemplate().aggregate(agg, PersonalizedList.class, Document.class).getMappedResults();
        return results;
    }    
    
}
