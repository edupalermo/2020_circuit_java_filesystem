package org.palermo.circuit.configuration;

public class ParameterMetadata {

    private final String name;
    private final DataType dataType;
    private final Direction direction;

    public ParameterMetadata(String name, DataType dataType, Direction direction) {
        this.name = name;
        this.dataType = dataType;
        this.direction = direction;
    }

    public static ParameterMetadata of(String name, DataType dataType, Direction direction) {
        return new ParameterMetadata(name, dataType, direction);
    }

    public String getName() {
        return name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public Direction getDirection() {
        return direction;
    }

    public enum DataType {
        FILE, STRING, ENUM, LONG, DOUBLE, DATE, DATE_TIME, UNICODE_CHAR
    }

    public enum Direction {
        INPUT, OUTPUT
    }

}
