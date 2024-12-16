package com.han_batang.back.common;

public interface ResponseMessage {
    String SUCCESS = "success.";
    
    String VALIDATION_FAIL = "Validation failed.";
    String DUPLICATE_ID = "Duplicate Id.";
    String DUPICATED_DATA = "Already Exist.";

    String SIGN_IN_FAIL = "Login information mismatch.";
    String CERTIFICATION_FAIL = "Certifiaction failed.";

    String MAIL_FAIL = "Mail send failed.";
    String DATABASE_ERROR = "Database error.";

    String UNAUTHORIZED = "No authorization.";

    String NO_DATA = "No data";
}
