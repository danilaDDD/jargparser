package ru.danila.argparser;

import ru.danila.argparser.argparserargs.ParserArgs;
import ru.danila.argparser.commandsrunner.CommandsRunner;

class SimpleArgParser implements ArgParser{
    private final ParserArgs args;

    public SimpleArgParser(ParserArgs args) {
        this.args = args;
    }

    @Override
    public void printCommandsInfo() {

    }

    @Override
    public CommandsRunner parse(String commandLine) {
        return null;
    }
}
