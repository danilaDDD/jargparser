package ru.danila.exceptions;

public class RequiredParamFieldNotSetException extends RuntimeException{
    public RequiredParamFieldNotSetException(String fieldName) {
        super(String.format("param field %s is required was not set", fieldName));
    }
}
