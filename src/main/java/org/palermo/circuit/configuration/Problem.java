package org.palermo.circuit.configuration;

import org.palermo.circuit.configuration.util.ProcessOutputSize;
import org.palermo.circuit.configuration.util.ProcessRelevantBits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Problem {

    private final ProblemType problemType;
    private final Map<String, ParameterMetadata> parameterMetadataMap;
    private final Map<String, Set<Integer>> relevantBitsMap;
    private final Map<String, Integer> outputSizeMap;

    private Problem(ProblemType problemType, Map<String,
                    ParameterMetadata> parameterMetadataMap,
                    Map<String, Set<Integer>> relevantBitsMap,
                    Map<String, Integer> outputSizeMap) {
        this.problemType = problemType;
        this.parameterMetadataMap = Collections.unmodifiableMap(parameterMetadataMap);
        this.relevantBitsMap = Collections.unmodifiableMap(relevantBitsMap);
        this.outputSizeMap = Collections.unmodifiableMap(outputSizeMap);
    }

    public static ProblemBuilder builder() {
        return new ProblemBuilder();
    }

    public ProblemType getProblemType() {
        return problemType;
    }

    public Map<String, ParameterMetadata> getParameterMetadataMap() {
        return parameterMetadataMap;
    }

    public enum ProblemType {
        /* With memories */
        STREAM,
        /* Only NAND ports */
        STATELESS
    }

    public static class ProblemBuilder {

        private final Map<String, ParameterMetadata> parameterMetadata = new TreeMap<>();
        private ProblemType problemType;
        private final List<Map<String, String>> trainingData = new ArrayList<>();

        public ProblemBuilder addArgument(ParameterMetadata parameterMetadata) {
            this.parameterMetadata.put(parameterMetadata.getName(), parameterMetadata);
            return this;
        }

        public ProblemBuilder type(ProblemType problemType) {
            this.problemType = problemType;
            return this;
        }

        public ProblemBuilder addTrainingData(Parameter... parameters) {
            Map<String, String> map = new TreeMap<>();
            for (Parameter parameter : parameters) {
                map.put(parameter.getName(), parameter.getValue());
            }
            trainingData.add(Collections.unmodifiableMap(map));
            return this;
        }

        public Problem build() {
            Map<String, Set<Integer>> relevantBitsMap = ProcessRelevantBits.process(problemType, parameterMetadata, trainingData);
            Map<String, Integer> outputSizeMap = ProcessOutputSize.process(parameterMetadata, trainingData);
            return new Problem(problemType, parameterMetadata, relevantBitsMap, outputSizeMap);
        }
    }
}
