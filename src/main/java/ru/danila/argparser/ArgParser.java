package ru.danila.argparser;

import ru.danila.argparser.param.CommandParam;
import ru.danila.exceptions.ParseCommandLineException;

import java.util.*;

public class ArgParser {
    private Set<CommandParam> paramSet;
    private CommandArgumentCollector collector;


    public void printInfo(){
        System.out.println(this);
    }

    public void parse(String commandArg) throws ParseCommandLineException {
        Map<CommandParam, List<String>> collectedParams = collector.collect(commandArg);

    }

    private ArgParser(Builder builder){
        paramSet = builder.paramSet;
        collector = new CommandArgumentCollector(paramSet);
    }

    @Override
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder();
        for(var param: paramSet)
            stringBuffer.append(param.toString()).append("\n");

        return stringBuffer.toString();
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        Set<CommandParam> paramSet = new HashSet<>();

        public Builder addParam(CommandParam param){
            paramSet.add(param);
            return this;
        }

        public ArgParser build(){
            return new ArgParser(this);
        }
    }
}
