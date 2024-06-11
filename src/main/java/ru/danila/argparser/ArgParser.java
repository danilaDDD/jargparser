package ru.danila.argparser;

import ru.danila.argparser.param.Param;
import ru.danila.argparser.param.ParamHandler;
import ru.danila.argparser.param.ParamType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArgParser {
    private Map<String, Param> paramDTOMap;
    private CommandArgParser commandArgParser;


    public void printInfo(){
        for(var entry: paramDTOMap.entrySet())
            System.out.println(entry.getValue());

        commandArgParser = new CommandArgParser(paramDTOMap);
    }

    public void parse(String commandArg){
        commandArgParser.parse(commandArg);
    }

    private ArgParser(Builder builder){
        handleParams(builder.params);
    }

    private void handleParams(List<Param> params) {
        for(Param param: params)
            paramDTOMap.put(param.getShortName(), param);
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        List<Param> params = new ArrayList<>();

        public Builder addHandler(Param param){
            params.add(param);
            return this;
        }

        public ArgParser build(){
            return new ArgParser(this);
        }
    }
}
