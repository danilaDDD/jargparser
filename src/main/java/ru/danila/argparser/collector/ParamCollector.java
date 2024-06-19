package ru.danila.argparser.collector;

import ru.danila.argparser.argparserargs.ParserArgs;
import ru.danila.argparser.param.KeyCommandParam;
import ru.danila.argparser.param.PositionCommandParam;

import java.util.*;
import java.util.stream.Collectors;

public class ParamCollector {
    private final Map<String, KeyCommandParam> shortNameToParam;
    private final Map<String, KeyCommandParam> fullNameToParam;
    private final List<PositionCommandParam> positionParamList;

    public ParamCollector(ParserArgs parserArgs) {
        Set<KeyCommandParam> keyParamsSet = parserArgs.getKeyParams();
        shortNameToParam = keyParamsSet.stream().collect(Collectors.toMap(KeyCommandParam::getShortName, p -> p));
        fullNameToParam = keyParamsSet.stream().collect(Collectors.toMap(KeyCommandParam::getFullName, p -> p));
        positionParamList = parserArgs.getPositionParams();
    }

    public CollectedResult collect(String commandLine){
        return null;
    }
}
