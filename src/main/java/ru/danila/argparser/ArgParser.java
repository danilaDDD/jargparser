package ru.danila.argparser;

import ru.danila.argparser.argparserargs.ParserArgs;
import ru.danila.argparser.commandsrunner.CommandsRunner;

public interface ArgParser {
    static ArgParser of(ParserArgs args) {
        return new SimpleArgParser(args);
    }

    void printCommandsInfo();

    CommandsRunner parse(String commandLine);
}
