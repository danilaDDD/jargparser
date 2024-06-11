package ru.danila.argparser;

import ru.danila.argparser.param.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArgParser {
    private Map<String, Param> paramDTOMap;
    private CommandArgParser commandArgParser;


    public void printInfo(){
        System.out.println(this);
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

    @Override
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder();
        for(var entry: paramDTOMap.entrySet())
            stringBuffer.append(entry.getValue()).append("\n");

        return stringBuffer.toString();
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        List<Param> params = new ArrayList<>();

        public Builder addParam(Param param){
            params.add(param);
            return this;
        }

        public ArgParser build(){
            return new ArgParser(this);
        }
    }
}
