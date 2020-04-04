package org.palermo.circuit.configuration.util;

import org.palermo.circuit.configuration.ParameterMetadata;
import org.palermo.circuit.configuration.ParameterMetadata.Direction;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class ProcessOutputSize {

    public static Map<String, Integer> process(Map<String, ParameterMetadata> parameterMetadataMap,
                                               List<Map<String, String>> trainingData) {

        Map<String, Integer> outputSizeMap = new TreeMap<>();

        for (Entry<String, ParameterMetadata> entry : parameterMetadataMap.entrySet()) {
            String parameterName = entry.getKey();
            ParameterMetadata parameterMetadata = entry.getValue();

            if (parameterMetadata.getDirection() == Direction.INPUT) {
                continue;
            }

            switch(parameterMetadata.getDataType()) {
            case ENUM:
                outputSizeMap.put(parameterName, processEnumType(parameterName, trainingData));
                break;
            default:
                throw new RuntimeException("Not implemented");
            }
        }

        return outputSizeMap;
    }

    private static Integer processEnumType(String parameterName, List<Map<String, String>> trainingData) {
        Set<String> set = new TreeSet<>();
        for (Map<String, String> map : trainingData) {
            set.add(map.get(parameterName));
        }
        return numberOfBitsNeeded(set.size());
    }

    protected static int numberOfBitsNeeded(int differentElements) {
        if (differentElements == 0) {
            throw new IllegalArgumentException("Illegal number of elements");
        }
        if (differentElements == 1) {
            return 1;
        }
        return (int) (Math.floor(Math.log(differentElements - 1) / Math.log(2))) + 1;
    }
}
