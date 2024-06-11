package ru.danila.tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.danila.argparser.ArgParser;
import ru.danila.argparser.param.Param;
import ru.danila.argparser.param.ParamType;
import ru.danila.handler.IntegerParamHandler;
import ru.danila.handler.StringParamHandler;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SuccessParseByArgParserTests {

    @ParameterizedTest
    @MethodSource(value = "getArgs")
    public void testCommandHandle(ArgParser argParser, String commandArg){
        argParser.parse(commandArg);
    }

    private static Stream<Arguments> getArgs(){
        return Stream.of(
                getRepeatedStringTestArguments(),
                getIntegerRepeatTestArguments()
        );
    }

    private static Arguments getRepeatedStringTestArguments(){
        List<String> expectedArgValues = List.of("key1=value1", "key2=value2", "key3=value3");

        Param param = Param.builder()
                .setShortName("e")
                .setFullName("env")
                .setRepeated(true)
                .setParamHandler(new StringParamHandler() {
                    @Override
                    public void handle(List<String> args) {
                        assertEquals(expectedArgValues, args);
                    }
                })
                .build();

        ArgParser argParser = ArgParser.builder().addParam(param).build();
        String commandArg = "-e key1=value1 --env key2=value2 -e key3=value3";

        return Arguments.of(argParser, commandArg);
    }

    private static Arguments getIntegerRepeatTestArguments(){
        List<Integer> expectedArg = List.of(1, 2, 3);

        Param param = Param.builder()
                .setRepeated(true)
                .setParamType(ParamType.INTEGER)
                .setShortName("i")
                .setFullName("int")
                .setParamHandler(new IntegerParamHandler() {
                    @Override
                    public void handle(List<Integer> arg) {
                        assertEquals(expectedArg, arg);
                    }
                }).build();

        String commandArg = "--int 1 -i 2 --int 3";
        ArgParser argParser = ArgParser.builder().addParam(param).build();

        return Arguments.of(argParser, commandArg);
    }
}