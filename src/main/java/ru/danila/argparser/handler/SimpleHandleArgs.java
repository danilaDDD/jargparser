package ru.danila.argparser.handler;

import ru.danila.argparser.exceptions.InvalidTypeParamException;
import ru.danila.argparser.param.KeyCommandParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleHandleArgs implements HandleArgs{
    Map<KeyCommandParam, Object> keyParamToArgument = new HashMap<>();
    List<Object> positionParamValues = new ArrayList<>();
    Map<String, KeyCommandParam> paramShortNameToKeyParam = new HashMap<>();

    public void setKeyParamValue(KeyCommandParam keyCommandParam, Object arg){
        paramShortNameToKeyParam.put(keyCommandParam.getShortName(), keyCommandParam);
        keyParamToArgument.put(keyCommandParam, arg);
    }

    public void addPositionParamValue(Object arg){
        positionParamValues.add(arg);
    }

    @Override
    public <R> R getPositionValue(int index) {
        try {
            Object value = this.positionParamValues.get(index);
            if (value == null)
                return null;

            return (R) value;
        }catch (ClassCastException e){
            throw new InvalidTypeParamException("Invalid type param given");
        }

    }

    @Override
    public <R> R getKeyValue(String paramShortName) {
        try {
            KeyCommandParam param = paramShortNameToKeyParam.get(paramShortName);
            if (param == null)
                return null;

            return (R) keyParamToArgument.get(param);
        }catch (ClassCastException e){
            throw new InvalidTypeParamException("Invalid type param given");
        }
    }
}
