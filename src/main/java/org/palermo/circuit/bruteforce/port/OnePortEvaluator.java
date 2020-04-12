package org.palermo.circuit.bruteforce.port;

import java.util.Map;

public class OnePortEvaluator implements PortEvaluator {

    @Override
    public int getValue(Map<String, String> parameters) {
        return 1;
    }

    @Override
    public byte[] getDescription() {
        throw new RuntimeException("Not Implemented");
    }

}
