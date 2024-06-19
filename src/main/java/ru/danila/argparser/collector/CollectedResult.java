package ru.danila.argparser.collector;

import ru.danila.argparser.param.KeyCommandParam;
import ru.danila.argparser.param.PositionCommandParam;

import java.util.List;
import java.util.Map;

public class CollectedResult{
    public Map<KeyCommandParam, List<String>> keyParamToValues;
    public Map<PositionCommandParam, String> positionParamToValue;

    public CollectedResult(Map<PositionCommandParam, String> positionParamToValue, Map<KeyCommandParam, List<String>> keyParamToValues) {
        this.positionParamToValue = positionParamToValue;
        this.keyParamToValues = keyParamToValues;
    }
}
