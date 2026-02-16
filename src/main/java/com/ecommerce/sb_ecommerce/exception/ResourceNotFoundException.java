package com.ecommerce.sb_ecommerce.exception;

public class ResourceNotFoundException extends RuntimeException {
    private String resourceName;
    private String field;
    private Long fieldId;
    private String fieldName;

    public ResourceNotFoundException(String resourceName, String field,Long fieldId) {
        super(String.format("%s not found with %s : %d", resourceName, field, fieldId));
        this.resourceName = resourceName;
        this.fieldId = fieldId;
        this.field = field;
    }

    public ResourceNotFoundException(String resourceName, String fieldName, String field) {
        super(String.format("%s not found with %s : %s", resourceName, field, fieldName));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.field = field;
    }
}
