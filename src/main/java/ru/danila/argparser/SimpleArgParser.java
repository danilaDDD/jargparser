package ru.danila.argparser;

import ru.danila.argparser.argparserargs.ParserArgs;
import ru.danila.argparser.collector.CollectedResult;
import ru.danila.argparser.collector.ParamCollector;
import ru.danila.argparser.commandsrunner.CommandsRunner;
import ru.danila.argparser.param.KeyCommandParam;
import ru.danila.argparser.param.PositionCommandParam;
import ru.danila.argparser.validators.ParsedResultValidator;
import ru.danila.argparser.validators.ParserArgsValidator;

class SimpleArgParser implements ArgParser{
    private final ParserArgs args;
    private ParamCollector paramCollector;
    private ParsedResultValidator parsedResultValidator;
    private CommandsRunnerFactory commandsRunnerFactory;

    public SimpleArgParser(ParserArgs args) {
        new ParserArgsValidator().validateOrThrow(args);
        this.args = args;
        this.paramCollector = new ParamCollector(args);
        this.parsedResultValidator = new ParsedResultValidator();
        this.commandsRunnerFactory = new CommandsRunnerFactory();
    }

    @Override
    public void printCommandsInfo() {
        System.out.println("position params: ");
        for(PositionCommandParam param: this.args.getPositionParams())
            System.out.println(param);
        System.out.println("key params: ");
        for(KeyCommandParam param: this.args.getKeyParams())
            System.out.println(param);
    }

    @Override
    public CommandsRunner parse(String commandLine) {
        CollectedResult collectedResult = paramCollector.collect(commandLine);
        parsedResultValidator.validateOrThrow(collectedResult);

        return commandsRunnerFactory.runnerFactory(collectedResult);
    }
}
