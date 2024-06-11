package ru.danila.argparser;

import ru.danila.argparser.param.Param;

import java.util.Map;

public class CommandArgParser {
    Map<String, Param> paramDTOMap;

    public CommandArgParser(Map<String, Param> paramDTOMap) {
        this.paramDTOMap = paramDTOMap;
    }

    public void parse(String commandArg){

    }
}
