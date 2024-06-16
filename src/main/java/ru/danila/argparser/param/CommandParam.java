package ru.danila.argparser.param;

public class CommandParam {
    private ParamType paramType;
    private boolean isRequired;
    private String description;

    public CommandParam(ParamType paramType, boolean isRequired, String description) {
        this.paramType = paramType;
        this.isRequired = isRequired;
        this.description = description;
    }

    public ParamType getParamType() {
        return paramType;
    }

    public void setParamType(ParamType paramType) {
        this.paramType = paramType;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
