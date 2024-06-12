package ru.danila.exceptions;

import java.io.IOException;

public class ParseCommandLineException extends IOException {
    public ParseCommandLineException(String s) {
        super(s);
    }
}
