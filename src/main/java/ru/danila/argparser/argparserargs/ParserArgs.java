package ru.danila.argparser.argparserargs;

import ru.danila.argparser.handler.CommandHandler;
import ru.danila.argparser.param.KeyCommandParam;
import ru.danila.argparser.param.PositionCommandParam;

import java.util.Deque;
import java.util.List;
import java.util.Set;

public interface ParserArgs {
    static SimpleParserArgs.Builder builder(){
        return SimpleParserArgs.builder();
    }

    Set<KeyCommandParam> getKeyParams();

    List<PositionCommandParam> getPositionParams();

    String getCommandLine();

    Deque<CommandHandler> getHandlersDeque();
}
