package com.edu.tcc.carbon.carbon.exceptions;

public class PostRequestException extends RuntimeException {
    
    public PostRequestException() {
        super("Invalid request.");
    }
    
    public PostRequestException(String message) {
        super(message);
    }

}
