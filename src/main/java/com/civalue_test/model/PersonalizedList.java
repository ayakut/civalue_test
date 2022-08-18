package com.civalue_test.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = PersonalizedList.COLLECTION_NAME)
@Data
public class PersonalizedList implements IAnyDocument{
    public static final String COLLECTION_NAME = "personalized_list";
    
    public static final String ID_FIELD = "shopperId";
    public static final String SHELF_FIELD = "shelf";
    

    @Indexed(unique = true)
    private String shopperId;        
    private List<Personalization> shelf = new ArrayList<>();
    
    
    @Override
    public List<String> getAllFields() {
        return Arrays.asList(ID_FIELD, SHELF_FIELD);
    }

    @Override
    public Object getFieldValue(String field) {
        switch (field){
            case ID_FIELD: return this.getShopperId();
            case SHELF_FIELD: return this.getShelf();
            default: return null;
        }
    }

    @Override
    public String getIdField() {
        return ID_FIELD;
    }

    @Override
    public String validateData() {
        if (this.shopperId == null || !StringUtils.hasText(this.shopperId)) return "Field <"+ID_FIELD+"> could not be empty!";
        this.shelf = this.shelf == null ? new ArrayList<>() : this.shelf;
        return "";
    }
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    public static class Personalization{
        private String productId;
        private Double relevancyScore;
    }
}
