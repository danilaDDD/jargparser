package ru.danila.argparser;

import ru.danila.argparser.argparserargs.ParserArgs;
import ru.danila.argparser.commandsrunner.CommandsRunner;

public interface ArgParser {
    void printCommandsInfo();

    CommandsRunner parse(ParserArgs args);
}
