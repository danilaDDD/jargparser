package ru.danila.exceptions;

import ru.danila.argparser.param.Param;

public class RequiredParamFieldNotSetException extends RuntimeException{
    public RequiredParamFieldNotSetException(String fieldName) {
        super(String.format("param field %s is required was not set", fieldName));
    }
}
