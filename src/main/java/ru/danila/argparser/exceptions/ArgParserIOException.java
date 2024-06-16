package ru.danila.argparser.exceptions;

import java.io.IOException;

public class ArgParserIOException extends IOException {
    public ArgParserIOException(String message) {
        super(message);
    }
}
