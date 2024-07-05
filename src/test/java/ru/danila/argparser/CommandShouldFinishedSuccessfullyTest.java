package ru.danila.argparser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.danila.argparser.argparserargs.ParserArgs;
import ru.danila.argparser.argparserargs.SimpleParserArgs;
import ru.danila.argparser.commandsrunner.CommandsRunner;
import ru.danila.argparser.exceptions.ParseCommandLineException;
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
    public void testRunCommandHandle(String commandLine, ParserArgs args){
        boolean success = false;
        try {
            ArgParser argParser = ArgParser.of(args);
            CommandsRunner commandsRunner = argParser.parse(commandLine);
            commandsRunner.runAll();
        }catch (CommandRunSuccessfullyThrowable e){
            success = true;
        } catch (ParseCommandLineException e) {
            throw new RuntimeException(e);
        }

        assertTrue(success);
    }

    @ParameterizedTest
    @MethodSource("getArgs")
    public void testPrintParamsInfo(String commandLine, ParserArgs args){
        ArgParser parser = ArgParser.of(args);
        parser.printCommandsInfo();
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
                commandWith10NumberPositionArgument(),
                commandWith1KeyParamAna2PositionParamArgument(),
                commandWith1KeyRepeatedAnd2PositionStringArgument(),
                commandWith1BooleanAnd2PositionNumberArgument(),
                commandMixedManyDifferentKeyAndDifferentPositionArgument()
        );
    }

    private static Arguments commandWith10NumberPositionArgument() {
        String command = "1 1 1 1 1 1 1 1 1 1";

        SimpleParserArgs.Builder builder = ParserArgs.builder();
        for (int i = 0; i < 5; i++) {
            builder = builder
                    .addPositionParam(new PositionCommandParam(ParamType.INTEGER))
                    .addPositionParam(new PositionCommandParam(ParamType.DOUBLE));
        }

        builder = builder.addHandler(new TestableCommandHandler((handleArgs) -> {
            for (int i = 0; i < 10; i++) {
                if(i % 2 == 0)
                    assertEquals(1, handleArgs.<Integer>getPositionValue(i));
                else
                    assertEquals(1, handleArgs.<Double>getPositionValue(i));
            }
        }));

        ParserArgs args = builder.build();
        return Arguments.of(command, args);
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

        return Arguments.of(command, args);
    }

    private static Arguments commandWith1KeyRepeatedAnd2PositionStringArgument(){
        String command = "--int 1 -i 2 --int 3 word1 word2";

        var param = KeyCommandParam.builder()
                .setRepeated(true)
                .setParamType(ParamType.INTEGER)
                .setShortName("i")
                .setFullName("int")
                .build();

        ParserArgs args = ParserArgs.builder()
                .addPositionParam(new PositionCommandParam())
                .addPositionParam(new PositionCommandParam())
                .addKeyParam(param)

                .addHandler(new TestableCommandHandler((handleArgs) -> {
                    assertEquals("word1", handleArgs.getPositionValue(0));
                    assertEquals("word2", handleArgs.getPositionValue(1));
                    assertEquals(handleArgs.getKeyValue("i"), List.of(1, 2, 3));
                }))
                .build();

        return Arguments.of(command, args);
    }

    private static Arguments commandWith1BooleanAnd2PositionNumberArgument() {
        String command = "1 1.22 -t";

        KeyCommandParam tParam = KeyCommandParam.builder()
                .setShortName("t")
                .setFullName("t")
                .setRequired(true)
                .setParamType(ParamType.NON_ARGUMENT)
                .build();

        ParserArgs args = ParserArgs.builder()
                .addPositionParam(new PositionCommandParam(ParamType.INTEGER))
                .addPositionParam(new PositionCommandParam(ParamType.DOUBLE))
                .addKeyParam(tParam)

                .addHandler(new TestableCommandHandler((handleArgs) -> {
                    assertEquals(1, handleArgs.<Integer>getPositionValue(0));
                    assertEquals(1.22d, handleArgs.<Double>getPositionValue(1));
                    assertEquals(true, handleArgs.getKeyValue("t"));
                }))

                .build();

        return Arguments.of(command, args);
    }

    private static Arguments commandMixedManyDifferentKeyAndDifferentPositionArgument(){
        String command = "-nm 1 --number 2.2 -nm 3 10 -t -p password 2.9 -i 1 --integer 2 three";

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
                .setRepeated(true)
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
                    assertEquals(List.of(1.0, 2.2, 3.0), handleArgs.getKeyValue("nm"));
                    assertEquals(true, handleArgs.getKeyValue("t"));
                    assertEquals("password", handleArgs.getKeyValue("p"));
                    assertEquals(List.of(1, 2), handleArgs.getKeyValue("i"));

                    assertEquals(10, handleArgs.<Integer>getPositionValue(0));
                    assertEquals(2.9d, handleArgs.getPositionValue(1));
                    assertEquals("three", handleArgs.getPositionValue(2));
                }))

                .build();

        return Arguments.of(command, args);
    }
}