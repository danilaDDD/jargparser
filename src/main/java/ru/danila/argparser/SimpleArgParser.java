package ru.danila.argparser;

import ru.danila.argparser.argparserargs.ParserArgs;
import ru.danila.argparser.collector.CollectedResult;
import ru.danila.argparser.collector.ParamCollector;
import ru.danila.argparser.commandsrunner.CommandsRunner;
import ru.danila.argparser.exceptions.ParseCommandLineException;
import ru.danila.argparser.param.KeyCommandParam;
import ru.danila.argparser.param.PositionCommandParam;
import ru.danila.argparser.validators.ParsedResultValidator;

class SimpleArgParser implements ArgParser{
    private final ParserArgs args;
    private final ParamCollector paramCollector;
    private final ParsedResultValidator parsedResultValidator;
    private final CommandsRunnerFactory commandsRunnerFactory;

    public SimpleArgParser(ParserArgs args) {
        this.args = args;
        this.paramCollector = new ParamCollector(args.getKeyParams());
        this.parsedResultValidator = new ParsedResultValidator(args);
        this.commandsRunnerFactory = new CommandsRunnerFactory(args);
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
    public CommandsRunner parse(String commandLine) throws ParseCommandLineException {
        CollectedResult collectedResult = paramCollector.collect(commandLine);
        parsedResultValidator.validateOrThrow(collectedResult);

        return commandsRunnerFactory.runnerFactory(collectedResult);
    }
}
