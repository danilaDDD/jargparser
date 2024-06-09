package ru.danila.argparser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArgParser {
    private Map<String, ParamDTO> paramDTOMap;
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

    private void handleParams(List<ParamDTO> params) {
        for(ParamDTO paramDTO: params)
            paramDTOMap.put(paramDTO.shortName, paramDTO);
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        List<ParamDTO> params = new ArrayList<>();

        public Builder addHandler(String shortName, String fullName, String description, boolean required, ParamType paramType, boolean repeated, ParamHandler handler){
            params.add(new ParamDTO(shortName, fullName, description, required, paramType, repeated, handler));
            return this;
        }
        
        public Builder addHandler(String shortName, String fullName, String description, ParamType paramType, ParamHandler handler){
            return addHandler(shortName, fullName, description, true, paramType, false, handler);
        }

        public ArgParser build(){
            return new ArgParser(this);
        }
    }

    public static class ParamDTO<T>{
        private final String shortName;
        private final String fullName;
        private final boolean required;
        private final ParamType paramType;
        private final boolean repeated;
        private final ParamHandler<T> handler;
        private final String description;

        public ParamDTO(String shortName, String fullName, String description, boolean required, ParamType paramType, boolean repeated, ParamHandler<T> handler) {
            this.fullName = fullName;
            this.shortName = shortName;
            this.required = required;
            this.paramType = paramType;
            this.repeated = repeated;
            this.handler = handler;
            this.description = description;
        }

        @Override
        public String toString() {
            return String.format("-%s --%s %s", shortName, fullName, description);
        }
    }
}
