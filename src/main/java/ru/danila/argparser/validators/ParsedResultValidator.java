package ru.danila.argparser.validators;

import ru.danila.argparser.argparserargs.ParserArgs;
import ru.danila.argparser.collector.CollectedResult;
import ru.danila.argparser.exceptions.ParseCommandLineException;
import ru.danila.argparser.param.KeyCommandParam;
import ru.danila.argparser.param.PositionCommandParam;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ParsedResultValidator {
    private final Set<KeyCommandParam> expectedKeyParamSet;
    private final List<PositionCommandParam> requiredExpectedPositionParams;

    public ParsedResultValidator(ParserArgs args) {
        expectedKeyParamSet = args.getKeyParams();

        List<PositionCommandParam> expectedPositionParamList = args.getPositionParams();
        requiredExpectedPositionParams = expectedPositionParamList.stream()
                .filter(PositionCommandParam::isRequired)
                .collect(Collectors.toList());
    }

    public void validateOrThrow(CollectedResult collectedResult) throws ParseCommandLineException {
        validateKeyParamValues(collectedResult.getKeyParamToValues());
        validatePositionParamValues(collectedResult.getPositionParamValues());
    }

    private void validateKeyParamValues(Map<KeyCommandParam, List<String>> givenKeyParamValuesMap) throws ParseCommandLineException {
        for(KeyCommandParam expected: this.expectedKeyParamSet){
            List<String> givenValues = givenKeyParamValuesMap.get(expected);

            if(expected.isRequired() && givenValues == null){
                throw new ParseCommandLineException(String.format("required param %s is not given", expected));
            }

            if(!expected.isRepeated() && givenValues != null && givenValues.size() > 1)
                throw new ParseCommandLineException(String.format("param %s is not repeated and more one value %s was given",
                        expected, givenValues));

        }
    }

    private void validatePositionParamValues(List<String> positionParamValues) throws ParseCommandLineException {
       if(requiredExpectedPositionParams.size() != positionParamValues.size())
           throw new ParseCommandLineException(String.format("expected %s position parameters but %s was given",
                   requiredExpectedPositionParams.size(), positionParamValues.size()));
    }
}
