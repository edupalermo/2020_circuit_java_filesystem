package org.palermo.circuit.configuration;

import org.palermo.circuit.configuration.ParameterMetadata.DataType;
import org.palermo.circuit.configuration.ParameterMetadata.Direction;

import java.util.Collections;
import java.util.Set;

public class EnrichedParameter {

    private final String name;
    private final DataType dataType;
    private final Direction direction;
    private final Set<Integer> relevantBits;

    private EnrichedParameter(String name, DataType dataType, Direction direction, Set<Integer> relevantBits) {
        this.name = name;
        this.dataType = dataType;
        this.direction = direction;
        this.relevantBits = Collections.unmodifiableSet(relevantBits);
    }

    public String getName() {
        return name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public Direction getDirection() {
        return direction;
    }

    public Set<Integer> getRelevantBits() {
        return relevantBits;
    }

    public static class EnrichedParameterBuilder {

        private String name;
        private DataType dataType;
        private Direction direction;
        private Set<Integer> relevantBits;

        public EnrichedParameterBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EnrichedParameterBuilder type(DataType dataType) {
            this.dataType = dataType;
            return this;
        }

        public EnrichedParameterBuilder direction(Direction direction) {
            this.direction = direction;
            return this;
        }

        public EnrichedParameterBuilder relevantBits(Set<Integer> relevantBits) {
            this.relevantBits = relevantBits;
            return this;
        }

        public EnrichedParameter build() {
            return new EnrichedParameter(name, dataType, direction, relevantBits);
        }
    }
}
