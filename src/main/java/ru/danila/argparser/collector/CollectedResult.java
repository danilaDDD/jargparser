package ru.danila.argparser.collector;

import ru.danila.argparser.param.KeyCommandParam;
import ru.danila.argparser.param.PositionCommandParam;

import java.util.List;
import java.util.Map;

public class CollectedResult{
    Map<KeyCommandParam, List<String>> keyParamToValues;
    List<String> positionParamValues;

    public CollectedResult(List<String> positionParamValues, Map<KeyCommandParam, List<String>> keyParamToValues) {
        this.positionParamValues = positionParamValues;
        this.keyParamToValues = keyParamToValues;
    }

    public Map<KeyCommandParam, List<String>> getKeyParamToValues() {
        return keyParamToValues;
    }

    public List<String> getPositionParamValues() {
        return positionParamValues;
    }
}
