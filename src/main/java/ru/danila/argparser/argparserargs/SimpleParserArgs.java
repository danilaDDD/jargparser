package ru.danila.argparser.argparserargs;
import ru.danila.argparser.handler.CommandHandler;
import ru.danila.argparser.param.KeyCommandParam;
import ru.danila.argparser.param.PositionCommandParam;
import ru.danila.argparser.exceptions.ArgParserRequiredParamException;

import java.util.*;

public class SimpleParserArgs implements ParserArgs{
    private final Set<KeyCommandParam> keyCommandParamSet;
    private List<PositionCommandParam> positionCommandParamList;
    private Deque<CommandHandler> handlers;

    private SimpleParserArgs(Builder builder){
        positionCommandParamList = builder.positionCommandParamList;
        keyCommandParamSet = builder.keyCommandParamSet;
        handlers = builder.handlers;
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
    public Deque<CommandHandler> getHandlersDeque() {
        return this.handlers;
    }

    @Override
    public String toString() {
        return String.format("ParserArgs{key arguments: %s, position arguments: %s}",keyCommandParamSet, positionCommandParamList);
    }

    public static class Builder{
        private final Set<KeyCommandParam> keyCommandParamSet = new HashSet<>();
        private final List<PositionCommandParam> positionCommandParamList = new LinkedList<>();
        private final Deque<CommandHandler> handlers = new ArrayDeque<>();

        public Builder addKeyParam(KeyCommandParam param){
            this.keyCommandParamSet.add(param);
            return this;
        }

        public Builder addPositionParam(PositionCommandParam param){
            this.positionCommandParamList.add(param);
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
            if(keyCommandParamSet.isEmpty() && positionCommandParamList.isEmpty())
                throw new ArgParserRequiredParamException("No command parameters added");

            if(handlers.isEmpty())
                throw new ArgParserRequiredParamException("handlers is empty");
        }
    }
}
