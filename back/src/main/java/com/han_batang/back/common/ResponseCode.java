package com.han_batang.back.common;

public interface ResponseCode {

    String SUCCESS = "SU";
    
    String VALIDATION_FAIL = "VF";
    String DUPLICATE_ID = "DI";
    String DUPICATED_DATA = "DD";

    String SIGN_IN_FAIL = "SF";
    String CERTIFICATION_FAIL = "CF";

    String MAIL_FAIL = "MF";
    String DATABASE_ERROR = "DBE";

    String UNAUTHORIZED = "UA";

    String NO_DATA = "ND";
} 