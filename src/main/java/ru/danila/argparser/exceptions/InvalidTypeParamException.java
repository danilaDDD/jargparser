package ru.danila.argparser.exceptions;

public class InvalidTypeParamException extends ClassCastException{
    public InvalidTypeParamException(String s) {
        super(s);
    }
}
