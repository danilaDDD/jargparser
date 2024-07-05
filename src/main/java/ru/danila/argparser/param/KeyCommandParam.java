package ru.danila.argparser.param;

import ru.danila.argparser.exceptions.RequiredParamFieldNotSetException;

import java.util.Objects;

/**
 * key command param
 * <p></p>short name String required</p>
 * <p>full name String required</p>
 * <p>paramType required with default ParamType. STRING</p>
 * <p>repeted with default false</p>
 * <p>required with default true</p>
 * <p>description must be null</p>
 */
public class KeyCommandParam extends CommandParam{
    private final String shortName;
    private final String fullName;
    private final boolean isRepeated;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyCommandParam that = (KeyCommandParam) o;
        return Objects.equals(shortName, that.shortName) && Objects.equals(fullName, that.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortName, fullName);
    }

    @Override
    public String toString() {
        return String.format("KeyCommandParam{short name=%s, full name=%s}", shortName, fullName);
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
