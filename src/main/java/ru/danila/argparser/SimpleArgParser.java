package ru.danila.argparser;

import ru.danila.argparser.argparserargs.ParserArgs;
import ru.danila.argparser.commandsrunner.CommandsRunner;

class SimpleArgParser implements ArgParser{
    @Override
    public void printCommandsInfo() {

    }

    @Override
    public CommandsRunner parse(ParserArgs args) {
        return null;
    }
}
