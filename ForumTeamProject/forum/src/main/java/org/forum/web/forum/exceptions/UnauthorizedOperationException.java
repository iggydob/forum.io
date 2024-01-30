package org.forum.web.forum.exceptions;

public class UnauthorizedOperationException extends RuntimeException{
    public UnauthorizedOperationException(String message){
        super(message);
    }
}
