package ru.danila.argparser.collector;

import ru.danila.argparser.exceptions.ParseCommandLineException;
import ru.danila.argparser.param.KeyCommandParam;
import ru.danila.argparser.param.ParamType;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ParamCollector {
    private final Map<String, KeyCommandParam> shortNameToParam;
    private final Map<String, KeyCommandParam> fullNameToParam;

    public ParamCollector(Set<KeyCommandParam> keyParamSet) {
        shortNameToParam = keyParamSet.stream().collect(Collectors.toMap(KeyCommandParam::getShortName, p -> p));
        fullNameToParam = keyParamSet.stream().collect(Collectors.toMap(KeyCommandParam::getFullName, p -> p));
    }

    public CollectedResult collect(String commandLine) throws ParseCommandLineException {
        Deque<String> splitCommandLineDeque = new ArrayDeque<>(List.of(commandLine.split(" ")));
        return collectFromList(splitCommandLineDeque);
    }
    // @FIXME it need to refactor
    private CollectedResult collectFromList(Deque<String> splitCommandLineDeque) throws ParseCommandLineException {
        List<String> positionParamValues = new ArrayList<>();
        Map<KeyCommandParam, List<String>> keyParamToValues = new HashMap<>();

        while (!splitCommandLineDeque.isEmpty()){
            String commandLineItem = splitCommandLineDeque.removeFirst();
            KeyCommandParam keyParam;
            if(commandLineItem.startsWith("--")){
                keyParam = fullNameToParam.get(commandLineItem.substring(2));
                throwParamExceptionIfNull(commandLineItem);
            } else if (commandLineItem.startsWith("-")) {
                keyParam = shortNameToParam.get(commandLineItem.substring(1));
                throwParamExceptionIfNull(commandLineItem);
            }else{
                positionParamValues.add(commandLineItem);
                continue;
            }

            //@FIXME It need to refactor immediately and check of unused this code
            if(keyParam.getParamType() != ParamType.NON_ARGUMENT){
                String value = splitCommandLineDeque.pollFirst();
                if(value == null)
                    throw new ParseCommandLineException("End of command line expected value and not given");
                else {
                    keyParamToValues.computeIfAbsent(keyParam, (key) -> new ArrayList<>()).add(value);
                }
            }else{
                String value = "true";
                keyParamToValues.computeIfAbsent(keyParam, (key) -> new ArrayList<>()).add(value);
            }

        }

        return new CollectedResult(positionParamValues, keyParamToValues);
    }



    private void throwParamExceptionIfNull(String commandLineItem) throws ParseCommandLineException {
        if(commandLineItem == null)
            throw new ParseCommandLineException(String.format("character %s is not found in available key commands", commandLineItem));
    }


}
