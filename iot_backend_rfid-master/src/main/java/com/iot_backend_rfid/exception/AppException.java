package com.iot_backend_rfid.exception;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {
    private String message;
    private HttpStatus httpStatus;

    public AppException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus=httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
