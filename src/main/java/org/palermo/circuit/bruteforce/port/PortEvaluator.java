package org.palermo.circuit.bruteforce.port;

import java.util.Map;

public interface PortEvaluator {
    int getValue(Map<String, String> parameters);

    byte[] getDescription();
}
