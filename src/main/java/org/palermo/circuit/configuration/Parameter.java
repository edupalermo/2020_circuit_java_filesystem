package org.palermo.circuit.configuration;

public class Parameter {

    private final String name;
    private final String value;

    private Parameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static Parameter of(String name, String value) {
        return new Parameter(name, value);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
