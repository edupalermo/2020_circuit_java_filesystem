package org.palermo.circuit.bruteforce.port;

import org.palermo.circuit.configuration.ParameterMetadata;

import java.util.Map;

public interface PortEvaluator {

    static final int TYPE_ZERO = 0x00;
    static final int TYPE_ONE = 0x01;
    static final int TYPE_INPUT = 0x02;
    static final int TYPE_NAND = 0x03;
    static final int TYPE_MEMORY = 0x04;

    int getValue(Map<String, ParameterMetadata> parameterMetadataMap, Map<String, String> parameters);

    byte[] getDescription();
}
