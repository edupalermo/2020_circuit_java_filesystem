package org.palermo.circuit.configuration;

public interface Result {

    boolean acceptable();

    String asString();

    boolean isBetterThan(Result result);

    boolean equals(Result result);
}
