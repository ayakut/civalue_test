package com.civalue_test.model;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = Product.COLLECTION_NAME)
@Data
public class Product implements IAnyDocument{
    public static final String COLLECTION_NAME = "product";
    
    public static final String ID_FIELD = "productId";
    public static final String CATEGORY_FIELD = "category";
    public static final String BRAND_FIELD = "brand";

    @Indexed(unique = true)
    private String productId;           
    private String category="";
    private String brand="";
    
    
    @Override
    public List<String> getAllFields() {
        return Arrays.asList(ID_FIELD, CATEGORY_FIELD, BRAND_FIELD);
    }

    @Override
    public Object getFieldValue(String field) {
        switch (field){
            case ID_FIELD: return this.getProductId();
            case CATEGORY_FIELD: return this.getCategory();
            case BRAND_FIELD: return this.getBrand();
            default: return null;
        }
    }

    @Override
    public String getIdField() {
        return ID_FIELD;
    }

    @Override
    public String validateData() {
        if (this.productId == null || !StringUtils.hasText(this.productId)) return "Field <"+ID_FIELD+"> could not be empty!";
        this.category = this.category == null ? "" : this.category;
        this.brand = this.brand == null ? "" : this.brand;
        return "";
    }
    
}
