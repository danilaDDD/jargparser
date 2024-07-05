package ru.danila.argparser;

import ru.danila.argparser.argparserargs.ParserArgs;
import ru.danila.argparser.collector.CollectedResult;
import ru.danila.argparser.commandsrunner.CommandsRunner;
import ru.danila.argparser.commandsrunner.SimpleCommandsRunner;
import ru.danila.argparser.exceptions.ParseCommandLineException;
import ru.danila.argparser.handler.CommandHandler;
import ru.danila.argparser.handler.HandleArgs;
import ru.danila.argparser.handler.SimpleHandleArgs;
import ru.danila.argparser.param.KeyCommandParam;
import ru.danila.argparser.param.ParamType;
import ru.danila.argparser.param.PositionCommandParam;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.function.Supplier;

public class CommandsRunnerFactory {
    private final ParserArgs parserArgs;

    public CommandsRunnerFactory(ParserArgs parserArgs) {
       this.parserArgs = parserArgs;
    }

    CommandsRunner runnerFactory(CollectedResult collectedResult) throws ParseCommandLineException {
        HandleArgs handleArgs = parsedValues(collectedResult);

        List<Runnable> runners = new ArrayList<>();
        for(CommandHandler handler: this.parserArgs.getHandlersDeque())
            runners.add(() -> handler.handle(handleArgs));

        return CommandsRunner.of(runners);
    }

    private HandleArgs parsedValues(CollectedResult collectedResult) throws ParseCommandLineException {
        SimpleHandleArgs handleArgs = new SimpleHandleArgs();
        appendKeyParamValues(handleArgs, collectedResult.getKeyParamToValues());
        appendPositionParamValues(handleArgs, collectedResult.getPositionParamValues());

        return handleArgs;
    }

    private void appendKeyParamValues(SimpleHandleArgs handleArgs, Map<KeyCommandParam, List<String>> givenKeyParamToValues) throws ParseCommandLineException {
        for(Map.Entry<KeyCommandParam, List<String>> entry: givenKeyParamToValues.entrySet()){
            KeyCommandParam keyParam = entry.getKey();

            Object parsedValue = parseValueOrThrow(keyParam.getParamType(), keyParam.isRepeated(), entry.getValue());
            handleArgs.setKeyParamValue(keyParam, parsedValue);
        }
    }

    private void appendPositionParamValues(SimpleHandleArgs handleArgs, List<String> givenPositionParamValues) throws ParseCommandLineException {
        List<PositionCommandParam> expectedParams = this.parserArgs.getPositionParams();
        for (int i = 0; i < givenPositionParamValues.size(); i++) {
            PositionCommandParam param = expectedParams.get(i);
            String stringValue = givenPositionParamValues.get(i);

            handleArgs.addPositionParamValue(parseValueOrThrow(param.getParamType(),
                    false, List.of(stringValue)));
        }
    }

    private Object parseValueOrThrow(ParamType paramType, boolean repeated, List<String> values) throws ParseCommandLineException {

       try {
           return tryParseValues(paramType, repeated, values);
       }catch (NumberFormatException e){
           throw new ParseCommandLineException(String.format("values %s not parsed to type %s", values, paramType));
       }
    }

    private Object tryParseValues(ParamType paramType, boolean repeated, List<String> values) {
        Map<ParamType, Supplier<Object>> valuesToObjectMap = Map.of(
                ParamType.STRING, () -> new ValuesToObject<>((string) -> string).get(values, repeated),
                ParamType.INTEGER, () -> new ValuesToObject<>(Integer::parseInt).get(values, repeated),
                ParamType.DOUBLE, () -> new ValuesToObject<>(Double::parseDouble).get(values, repeated),
                ParamType.NON_ARGUMENT, () -> new ValuesToObject<>(Boolean::parseBoolean).get(values, false)
        );

        return valuesToObjectMap.get(paramType).get();
    }


}

class ValuesToObject<T>{
    private Function<String, T> parser;

    public ValuesToObject(Function<String, T> parser) {
        this.parser = parser;
    }

    public Object get(List<String> values, boolean repeated){
        if(repeated)
            return values.stream().map(parser).collect(Collectors.toList());
        else
            return parser.apply(values.get(0));
    }
}
