package ru.danila.argparser.param;

import ru.danila.argparser.exceptions.RequiredParamFieldNotSetException;

public class KeyCommandParam extends CommandParam{
    private String shortName;
    private String fullName;
    private boolean isRepeated;

    private KeyCommandParam(Builder builder) {
        super(builder.paramType, builder.isRequired, builder.description);
        this.shortName = builder.shortName;
        this.fullName = builder.fullName;
        this.isRepeated = builder.isRepeated;
    }

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isRepeated() {
        return isRepeated;
    }

    public static class Builder{
        private ParamType paramType = ParamType.STRING;
        private boolean isRequired = true;
        private String description;
        private String shortName;
        private String fullName;
        private boolean isRepeated = false;


        public Builder setShortName(String shortName) {
            this.shortName = shortName;
            return this;
        }

        public Builder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder setRepeated(boolean repeated) {
            isRepeated = repeated;
            return this;
        }

        public Builder setParamType(ParamType paramType) {
            this.paramType = paramType;
            return this;
        }

        public Builder setRequired(boolean required) {
            this.isRequired = required;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public KeyCommandParam build(){
            validateOrThrow();
            return new KeyCommandParam(this);
        }

        private void validateOrThrow() {
            if(this.shortName == null || this.shortName.isEmpty())
                throw new RequiredParamFieldNotSetException("shortName");

            if(this.fullName == null || this.fullName.isEmpty())
                throw new RequiredParamFieldNotSetException("fullName");
        }
    }

    public static Builder builder(){
        return new Builder();
    }
}
