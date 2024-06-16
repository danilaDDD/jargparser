package ru.danila.argparser.exceptions;

public class ArgParserRequiredParamException extends RuntimeException {
    public ArgParserRequiredParamException(String message) {
        super(message);
    }
}
