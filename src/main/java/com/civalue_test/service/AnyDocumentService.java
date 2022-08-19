package com.civalue_test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.civalue_test.model.IAnyDocument;

@Service
public abstract class AnyDocumentService extends AnyService{

    @Autowired
    public AnyDocumentService(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    public void upsertDocument(IAnyDocument document, Class<?> documentClass) throws Exception{
        String err = document.validateData();
        if (err != "") throw new Exception(err);
        Update update = new Update();
        for (String field : document.getAllFields()) {
            update.set(field, document.getFieldValue(field));
        }
        this.getTemplate().findAndModify(
            new Query(Criteria.where(document.getIdField()).is(document.getFieldValue(document.getIdField()))), 
            update, 
            new FindAndModifyOptions().upsert(true), 
            documentClass);
    }

}
