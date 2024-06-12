package ru.danila.argparser;

import ru.danila.argparser.param.CommandParam;
import ru.danila.argparser.param.ParamType;
import ru.danila.exceptions.ParseCommandLineException;

import java.util.*;
class CommandArgumentCollector {
    private Map<String, CommandParam> shortNameToParam, fullNameToParam;

    public CommandArgumentCollector(Set<CommandParam> params) {
        prepare(params);
    }

    private void prepare(Set<CommandParam> params) {
        shortNameToParam = new HashMap<>();
        fullNameToParam = new HashMap<>();

        for(var param: params){
            shortNameToParam.put(param.getShortName(), param);
            fullNameToParam.put(param.getFullName(), param);
        }
    }

    public Map<CommandParam, List<String>> collect(String commandLine) throws ParseCommandLineException {
        Deque<String> splitCommandLineDeque = new ArrayDeque<>(List.of(commandLine.split(" ")));

        return collectFromList(splitCommandLineDeque);
    }

    private Map<CommandParam, List<String>> collectFromList(Deque<String> splitCommandLineDeque) throws ParseCommandLineException {
        Map<CommandParam, List<String>> result = new HashMap<>();

        if(splitCommandLineDeque.isEmpty())
            throw new ParseCommandLineException("Command param not given");

        while(!splitCommandLineDeque.isEmpty()){
            String stringParam = splitCommandLineDeque.pollFirst();
            CommandParam commandParam = getParamOrThrow(stringParam);
            appendValueIfExist(commandParam, splitCommandLineDeque, result);
        }

        return result;
    }


    private CommandParam getParamOrThrow(String stringParam) throws ParseCommandLineException {
        CommandParam param;
        if(stringParam.startsWith("--")){
            String fullParamName = stringParam.replace("--", "");
            param = fullNameToParam.get(fullParamName);

        } else if (stringParam.startsWith("-")) {
            String shortName = stringParam.replace("-", "");
            param = shortNameToParam.get(shortName);

        }else
            throw new ParseCommandLineException(String.format("Invalid literal around \"%s\"", stringParam));

        if(param == null)
            throw new ParseCommandLineException(String.format("parameter \"%s\" not found", stringParam));

        return param;
    }

    private void appendValueIfExist(CommandParam commandParam, Deque<String> splitCommandLineDeque,
                                    Map<CommandParam, List<String>> result) throws ParseCommandLineException {
        if(commandParam.getParamType() != ParamType.NON_ARGUMENT){
            // or throw NoSuchElementException while handed above
            String value = splitCommandLineDeque.pollFirst();
            if(value == null)
                throw new ParseCommandLineException(String.format("command line finish without value for param \"%s\"", commandParam));

            if(result.containsKey(commandParam))
                result.get(commandParam).add(value);
            else
                result.put(commandParam, new ArrayList<>());
        }
    }


}
