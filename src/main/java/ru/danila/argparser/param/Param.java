package ru.danila.argparser.param;

import ru.danila.exceptions.RequiredParamFieldNotSetException;
import ru.danila.handler.ParamHandler;

public class Param{
    private final String shortName;
    private final String fullName;
    private final boolean required;
    private final ParamType paramType;
    private final boolean repeated;
    private final ParamHandler handler;
    private final String description;

    public static class Builder{
        private String shortName = null;
        private String fullName = null;
        private boolean required = true;
        private ParamType paramType = ParamType.STRING;
        private boolean repeated = false;
        private ParamHandler handler = null;
        private String description = "";

        public Builder setShortName(String shortName){
            this.shortName = shortName;
            return this;
        }

        public Builder setFullName(String fullName){
            this.fullName = fullName;
            return this;
        }

        public Builder setParamHandler(ParamHandler paramHandler){
            this.handler = paramHandler;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setParamType(ParamType paramType) {
            this.paramType = paramType;
            return this;
        }

        public Builder setRepeated(boolean repeated) {
            this.repeated = repeated;
            return this;
        }

        public Builder setRequired(boolean required) {
            this.required = required;
            return this;
        }

        public Param build(){
            if(this.shortName == null)
                throw new RequiredParamFieldNotSetException("short name");
            if(this.fullName == null)
                throw new RequiredParamFieldNotSetException("full name");
            if(this.handler == null)
                throw new RequiredParamFieldNotSetException("handler");

            return new Param(this);
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    private Param(Builder builder) {
        this.fullName = builder.fullName;
        this.shortName = builder.shortName;
        this.required = builder.required;
        this.paramType = builder.paramType;
        this.repeated = builder.repeated;
        this.handler = builder.handler;
        this.description = builder.description;
    }

    public String getDescription() {
        return description;
    }

    public String getFullName() {
        return fullName;
    }

    public ParamHandler getHandler() {
        return handler;
    }

    public ParamType getParamType() {
        return paramType;
    }

    public boolean isRepeated() {
        return repeated;
    }

    public boolean isRequired() {
        return required;
    }

    public String getShortName() {
        return shortName;
    }

    @Override
    public String toString() {
        return String.format("-%s --%s %s", shortName, fullName, description);
    }
}
