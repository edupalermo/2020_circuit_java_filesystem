package org.palermo.circuit.bruteforce.port;

import org.palermo.circuit.configuration.ParameterMetadata;
import org.palermo.circuit.util.DataTypeUtil;

import java.util.Map;

public class InputPortEvaluator implements PortEvaluator {

    private final ParameterMetadata parameterMedatada;
    private final int relevantBit;

    public InputPortEvaluator(ParameterMetadata parameterMedatada, int relevantBit) {
        this.parameterMedatada = parameterMedatada;
        this.relevantBit = relevantBit;
    }

    @Override
    public int getValue(Map<String, String> parameters) {
        return DataTypeUtil.getBit(parameterMedatada, parameters.get(parameterMedatada.getName()), relevantBit);
    }

    @Override
    public byte[] getDescription() {
        throw new RuntimeException("Not Implemented");
    }

}
