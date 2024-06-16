package ru.danila.argparser;

import ru.danila.argparser.argparserargs.ParserArgs;
import ru.danila.argparser.commandsrunner.CommandsRunner;

public interface ArgParser {
    static ArgParser of() {
        return new SimpleArgParser();
    }

    void printCommandsInfo();

    CommandsRunner parse(ParserArgs args);
}
