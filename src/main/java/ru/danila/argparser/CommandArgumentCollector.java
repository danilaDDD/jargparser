//package ru.danila.argparser;
//
//import ru.danila.argparser.param.ParamType;
//import ru.danila.argparser.exceptions.ParseCommandLineException;
//
//import java.util.*;
//class CommandArgumentCollector {
//    private Map<String, SimpleCommandParam> shortNameToParam, fullNameToParam;
//
//    public CommandArgumentCollector(Set<SimpleCommandParam> params) {
//        prepare(params);
//    }
//
//    private void prepare(Set<SimpleCommandParam> params) {
//        shortNameToParam = new HashMap<>();
//        fullNameToParam = new HashMap<>();
//
//        for(var param: params){
//            shortNameToParam.put(param.getShortName(), param);
//            fullNameToParam.put(param.getFullName(), param);
//        }
//    }
//
//    public Map<SimpleCommandParam, List<String>> collect(String commandLine) throws ParseCommandLineException {
//        Deque<String> splitCommandLineDeque = new ArrayDeque<>(List.of(commandLine.split(" ")));
//
//        return collectFromList(splitCommandLineDeque);
//    }
//
//    private Map<SimpleCommandParam, List<String>> collectFromList(Deque<String> splitCommandLineDeque) throws ParseCommandLineException {
//        Map<SimpleCommandParam, List<String>> result = new HashMap<>();
//
//        if(splitCommandLineDeque.isEmpty())
//            throw new ParseCommandLineException("Command param not given");
//
//        while(!splitCommandLineDeque.isEmpty()){
//            String stringParam = splitCommandLineDeque.pollFirst();
//            SimpleCommandParam simpleCommandParam = getParamOrThrow(stringParam);
//            appendValueIfExist(simpleCommandParam, splitCommandLineDeque, result);
//        }
//
//        return result;
//    }
//
//
//    private SimpleCommandParam getParamOrThrow(String stringParam) throws ParseCommandLineException {
//        SimpleCommandParam param;
//        if(stringParam.startsWith("--")){
//            String fullParamName = stringParam.replace("--", "");
//            param = fullNameToParam.get(fullParamName);
//
//        } else if (stringParam.startsWith("-")) {
//            String shortName = stringParam.replace("-", "");
//            param = shortNameToParam.get(shortName);
//
//        }else
//            throw new ParseCommandLineException(String.format("Invalid literal around \"%s\"", stringParam));
//
//        if(param == null)
//            throw new ParseCommandLineException(String.format("parameter \"%s\" not found", stringParam));
//
//        return param;
//    }
//
//    private void appendValueIfExist(SimpleCommandParam simpleCommandParam, Deque<String> splitCommandLineDeque,
//                                    Map<SimpleCommandParam, List<String>> result) throws ParseCommandLineException {
//        if(simpleCommandParam.getParamType() != ParamType.NON_ARGUMENT){
//            String value = splitCommandLineDeque.pollFirst();
//            if(value == null)
//                throw new ParseCommandLineException(String.format("command line finish without value for param \"%s\"", simpleCommandParam));
//
//            if(result.containsKey(simpleCommandParam))
//                result.get(simpleCommandParam).add(value);
//            else
//                result.put(simpleCommandParam, new ArrayList<>());
//        }
//    }
//
//
//}
