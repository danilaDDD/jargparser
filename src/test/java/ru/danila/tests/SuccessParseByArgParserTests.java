package ru.danila.tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.danila.argparser.ArgParser;
import ru.danila.argparser.argparserargs.ParserArgs;
import ru.danila.argparser.commandsrunner.CommandsRunner;
import ru.danila.argparser.handler.CommandHandler;
import ru.danila.argparser.handler.HandleArgs;
import ru.danila.argparser.param.KeyCommandParam;
import ru.danila.argparser.param.ParamType;
import ru.danila.argparser.param.PositionCommandParam;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SuccessParseByArgParserTests {
    private final ArgParser argParser = ArgParser.of();

    @ParameterizedTest
    @MethodSource(value = "getArgs")
    public void testRunSyncCommandHandle(ParserArgs args){
        boolean success = false;
        try {
            CommandsRunner commandsRunner = argParser.parse(args);
            commandsRunner.runAllSync();
        }catch (CommandRunSuccessfullyThrowable e){
            success = true;
        }

        assertTrue(success);
    }

    @ParameterizedTest
    @MethodSource(value = "getArgs")
    public void testRunAsyncCommandHandle(ParserArgs args){
        boolean success = false;
        try {
            CommandsRunner commandsRunner = argParser.parse(args);
            commandsRunner.runAllAsync();
        }catch (CommandRunSuccessfullyThrowable e){
            success = true;
        }

        assertTrue(success);
    }

    private static <T> boolean equalsPositionValues(HandleArgs handleArgs, List<T> expectedValues){
        boolean equals = true;
        for (int i = 0; i < expectedValues.size(); i++) {
            if(!expectedValues.get(i).equals(handleArgs.<String>getPositionValue(0))){
                equals = false;
                break;
            }
        }

        return equals;
    }

    private static class TestableCommandHandler implements CommandHandler{
        private CommandHandler commandHandler;

        public TestableCommandHandler(CommandHandler commandHandler) {
            this.commandHandler = commandHandler;
        }

        @Override
        public void handle(HandleArgs args) {
            commandHandler.handle(args);
            throw new CommandRunSuccessfullyThrowable();
        }
    }

    private static class CommandRunSuccessfullyThrowable extends RuntimeException{

        public CommandRunSuccessfullyThrowable() {
            super("Command successfully");
        }
    }

    private static Stream<Arguments> getArgs(){
        return Stream.of(
                getRepeatedStringTestArguments(),
                getIntegerRepeatTestArguments()
        );
    }

    private static Arguments getRepeatedStringTestArguments(){
        List<String> expectedArgValues = List.of("key1=value1", "key2=value2", "key3=value3");
        KeyCommandParam param = KeyCommandParam.builder()
                .setShortName("e")
                .setFullName("env")
                .setRepeated(true)
                .build();

        ParserArgs args = ParserArgs.builder()
                .addKeyParam(param)
                .addPositionParam(new PositionCommandParam(ParamType.INTEGER))
                .addPositionParam(new PositionCommandParam(ParamType.DOUBLE))
                .setCommandArg("1 1.2 -e key1=value1 --env key2=value2 -e key3=value3")
                .build();

        return Arguments.of(args);
    }

    private static Arguments getIntegerRepeatTestArguments(){
        String shortName = "i";
        var param = KeyCommandParam.builder()
                .setRepeated(true)
                .setParamType(ParamType.INTEGER)
                .setShortName(shortName)
                .setFullName("int")
                .build();

        List<Integer> expectedKeyValues = List.of(1, 2, 3);
        List<String> expectedPositionValues = List.of("word1", "word2");

        ParserArgs args = ParserArgs.builder()
                .addPositionParam(new PositionCommandParam())
                .addKeyParam(param)
                .setCommandArg("--int 1 -i 2 --int 3 word1 word2")
                .addHandler(new TestableCommandHandler((handleArgs) -> {
                    assertTrue(equalsPositionValues(handleArgs, expectedPositionValues));
                    assertEquals(handleArgs.<List<Integer>>getKeyValue("i"), expectedKeyValues);
                }))
                .build();

        return Arguments.of(args);
    }
}