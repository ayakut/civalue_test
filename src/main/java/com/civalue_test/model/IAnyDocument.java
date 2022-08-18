package com.civalue_test.model;

import java.util.List;

public interface IAnyDocument {
    public List<String> getAllFields();
    public Object getFieldValue(String field);
    public String getIdField();
    public String validateData();
}
