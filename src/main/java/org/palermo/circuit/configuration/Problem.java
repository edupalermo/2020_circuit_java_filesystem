package org.palermo.circuit.configuration;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class Problem {

    private final ProblemType problemType;
    private final Map<String, ParameterMetadata> parameterMetadata;

    private Problem(ProblemType problemType, Map<String, ParameterMetadata> parameterMetadata) {
        this.problemType = problemType;
        this.parameterMetadata = Collections.unmodifiableMap(parameterMetadata);
    }

    public static ProblemBuilder builder() {
        return new ProblemBuilder();
    }

    public ProblemType getProblemType() {
        return problemType;
    }

    public Map<String, ParameterMetadata> getParameterMetadata() {
        return parameterMetadata;
    }

    public enum ProblemType {
        STREAM, STATELESS
    }

    public static class ProblemBuilder {

        private final Map<String, ParameterMetadata> parameterMetadata = new TreeMap<>();
        private ProblemType problemType;

        public ProblemBuilder addArgument(ParameterMetadata parameterMetadata) {
            this.parameterMetadata.put(parameterMetadata.getName(), parameterMetadata);
            return this;
        }

        public ProblemBuilder type(ProblemType problemType) {
            this.problemType = problemType;
            return this;
        }

        public Problem build() {
            return new Problem(problemType, parameterMetadata);
        }
    }
}
