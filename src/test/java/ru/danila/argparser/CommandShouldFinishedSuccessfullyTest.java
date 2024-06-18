package ru.danila.argparser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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


public class CommandShouldFinishedSuccessfullyTest {

    @ParameterizedTest
    @MethodSource(value = "getArgs")
    public void testRunSyncCommandHandle(ParserArgs args, String commandLine){
        boolean success = false;
        try {
            ArgParser argParser = ArgParser.of(args);
            CommandsRunner commandsRunner = argParser.parse(commandLine);
            commandsRunner.runAllSync();
        }catch (CommandRunSuccessfullyThrowable e){
            success = true;
        }

        assertTrue(success);
    }

    @ParameterizedTest
    @MethodSource(value = "getArgs")
    public void testRunAsyncCommandHandle(ParserArgs args, String commandLine){
        boolean success = false;
        try {
            ArgParser argParser = ArgParser.of(args);
            CommandsRunner commandsRunner = argParser.parse(commandLine);
            commandsRunner.runAllAsync();
        }catch (CommandRunSuccessfullyThrowable e){
            success = true;
        }

        assertTrue(success);
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
                commandWith1KeyParamAna2PositionParamArgument(),
                CommandWith1KeyRepeatedAnd2PositionStringArgument(),
                CommandMixedManyDifferentKeyAndDifferentPositionArgument()
        );
    }

    private static Arguments commandWith1KeyParamAna2PositionParamArgument(){
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

                .addHandler(new TestableCommandHandler((handleArgs) -> {
                    assertEquals(1, handleArgs.<Integer>getPositionValue(0));
                    assertEquals(1.2d, handleArgs.<Double>getPositionValue(1));
                    assertEquals(expectedArgValues, handleArgs.getKeyValue("e"));
                }))

                .build();
        String command = "1 1.2 -e key1=value1 --env key2=value2 -e key3=value3";

        return Arguments.of(args, command);
    }

    private static Arguments CommandWith1KeyRepeatedAnd2PositionStringArgument(){
        var param = KeyCommandParam.builder()
                .setRepeated(true)
                .setParamType(ParamType.INTEGER)
                .setShortName("i")
                .setFullName("int")
                .build();

        ParserArgs args = ParserArgs.builder()
                .addPositionParam(new PositionCommandParam())
                .addKeyParam(param)

                .addHandler(new TestableCommandHandler((handleArgs) -> {
                    assertEquals("word1", handleArgs.getPositionValue(0));
                    assertEquals("word2", handleArgs.getPositionValue(1));
                    assertEquals(handleArgs.getKeyValue("i"), List.of(1, 2, 3));
                }))
                .build();
        String command = "--int 1 -i 2 --int 3 word1 word2";

        return Arguments.of(args, command);
    }

    private static Arguments CommandMixedManyDifferentKeyAndDifferentPositionArgument(){
        KeyCommandParam nmParam, tParam, pParam, iParam;
        nmParam = KeyCommandParam.builder()
                .setParamType(ParamType.DOUBLE)
                .setShortName("nm")
                .setFullName("number")
                .setRequired(true)
                .setRepeated(true)
                .setDescription("number")
                .build();

        tParam = KeyCommandParam.builder()
                .setParamType(ParamType.NON_ARGUMENT)
                .setShortName("t")
                .setFullName("t")
                .setRequired(false)
                .setRepeated(false)
                .build();

        pParam = KeyCommandParam.builder()
                .setShortName("p")
                .setFullName("ps")
                .setRepeated(false)
                .setRequired(false)
                .build();

        iParam = KeyCommandParam.builder()
                .setParamType(ParamType.INTEGER)
                .setShortName("i")
                .setFullName("integer")
                .setRepeated(false)
                .setRequired(true)
                .build();

        ParserArgs args = ParserArgs.builder()
                .addKeyParam(pParam)
                .addKeyParam(iParam)
                .addKeyParam(nmParam)
                .addKeyParam(tParam)
                .addPositionParam(new PositionCommandParam(ParamType.INTEGER)) // 10
                .addPositionParam(new PositionCommandParam(ParamType.DOUBLE)) // 2.9
                .addPositionParam(new PositionCommandParam()) // three

                .addHandler(new TestableCommandHandler((handleArgs) ->{
                    assertEquals(List.of(1d, 2.2, 3), handleArgs.getKeyValue("nm"));
                    assertEquals(true, handleArgs.getKeyValue("t"));
                    assertEquals("password", handleArgs.getKeyValue("p"));
                    assertEquals(List.of(1, 2), handleArgs.getKeyValue("i"));

                    assertEquals(10, handleArgs.<Integer>getPositionValue(0));
                    assertEquals(2.9d, handleArgs.getPositionValue(1));
                    assertEquals("three", handleArgs.getPositionValue(2));
                }))

                .build();
        String command = "-nm 1 --number 2.2 -nm 3 10 -t -p password 2.9 -i 1 --integer 2 three";
        return Arguments.of(args, command);
    }
}