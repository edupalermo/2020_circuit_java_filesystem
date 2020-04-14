package org.palermo.circuit.bruteforce.port;

import org.palermo.circuit.configuration.ParameterMetadata;
import org.palermo.circuit.util.DataTypeUtil;

import java.util.Map;

public class InputPortEvaluator implements PortEvaluator {

    private final String parameterName;
    private final int relevantBit;

    public InputPortEvaluator(String parameterName, int relevantBit) {
        this.parameterName = parameterName;
        this.relevantBit = relevantBit;
    }

    @Override
    public int getValue(Map<String, ParameterMetadata> parameterMetadataMap, Map<String, String> parameters) {
        ParameterMetadata parameterMetadata = parameterMetadataMap.get(parameterName);
        return DataTypeUtil.getBit(parameterMetadata, parameters.get(parameterMetadata.getName()), relevantBit);
    }

    @Override
    public byte[] getDescription() {
        throw new RuntimeException("Not Implemented");
    }

}
