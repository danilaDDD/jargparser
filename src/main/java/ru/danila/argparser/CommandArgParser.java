package ru.danila.argparser;

import java.util.Map;

public class CommandArgParser {
    Map<String, ArgParser.ParamDTO> paramDTOMap;

    public CommandArgParser(Map<String, ArgParser.ParamDTO> paramDTOMap) {
        this.paramDTOMap = paramDTOMap;
    }

    public void parse(String commandArg){

    }
}
