package ru.danila.argparser.param;

public class PositionCommandParam extends CommandParam{
    public PositionCommandParam(ParamType paramType, boolean isRequired, String description) {
        super(paramType, isRequired, description);
    }

    public PositionCommandParam(ParamType paramType, boolean isRequired) {
        this(paramType, isRequired, null);
    }

    public PositionCommandParam(ParamType paramType) {
        this(paramType, true, null);
    }

    public PositionCommandParam() {
        this(ParamType.STRING, true, null);
    }
}
