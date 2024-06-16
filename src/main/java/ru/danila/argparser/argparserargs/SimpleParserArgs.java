package ru.danila.argparser.argparserargs;
import ru.danila.argparser.handler.CommandHandler;
import ru.danila.argparser.param.KeyCommandParam;
import ru.danila.argparser.param.PositionCommandParam;
import ru.danila.argparser.exceptions.ArgParserRequiredParamException;

import java.util.*;

class SimpleParserArgs implements ParserArgs{
    private final Set<KeyCommandParam> keyCommandParamSet;
    private List<PositionCommandParam> positionCommandParamList;
    private String commandArgs;
    private Deque<CommandHandler> handlers;

    private SimpleParserArgs(Builder builder){
        positionCommandParamList = builder.positionCommandParamList;
        keyCommandParamSet = builder.keyCommandParamSet;
        commandArgs = builder.commandArg;
        handlers = builder.handlers;
    }

    @Override
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder();
        for(var param: keyCommandParamSet)
            stringBuffer.append(param.toString()).append("\n");

        return stringBuffer.toString();
    }

    public static Builder builder(){
        return new Builder();
    }

    @Override
    public Set<KeyCommandParam> getKeyParams() {
        return this.keyCommandParamSet;
    }

    @Override
    public List<PositionCommandParam> getPositionParams() {
        return this.positionCommandParamList;
    }

    @Override
    public String getCommandLine() {
        return this.commandArgs;
    }

    @Override
    public Deque<CommandHandler> getHandlersDeque() {
        return this.handlers;
    }

    public static class Builder{
        private final Set<KeyCommandParam> keyCommandParamSet = new HashSet<>();
        private List<PositionCommandParam> positionCommandParamList = new LinkedList<>();
        private String commandArg;
        private Deque<CommandHandler> handlers = new ArrayDeque<>();

        public Builder addKeyParam(KeyCommandParam param){
            this.keyCommandParamSet.add(param);
            return this;
        }

        public Builder addPositionParam(PositionCommandParam param){
            this.positionCommandParamList.add(param);
            return this;
        }

        public Builder setCommandArg(String commandArg) {
            this.commandArg = commandArg;
            return this;
        }

        public Builder addHandler(CommandHandler handler){
            this.handlers.add(handler);
            return this;
        }

        public SimpleParserArgs build(){
            validateOrThrow();
            return new SimpleParserArgs(this);
        }

        private void validateOrThrow(){
            if(keyCommandParamSet.isEmpty())
                throw new ArgParserRequiredParamException("No command parameters added");

            if(commandArg == null || commandArg.isEmpty())
                throw new ArgParserRequiredParamException("command line is empty or not given");

            if(handlers.isEmpty())
                throw new ArgParserRequiredParamException("handlers is empty");
        }
    }
}
