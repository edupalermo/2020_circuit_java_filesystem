package org.palermo.circuit;

import org.palermo.circuit.configuration.Parameter;
import org.palermo.circuit.configuration.ParameterMetadata;
import org.palermo.circuit.configuration.ParameterMetadata.DataType;
import org.palermo.circuit.configuration.ParameterMetadata.Direction;
import org.palermo.circuit.configuration.Problem;
import org.palermo.circuit.configuration.Problem.ProblemType;
import org.palermo.circuit.configuration.TrainingSet;

public class Main {

    public static void main(String[] args) {

        Problem problem = Problem.builder()
                .type(ProblemType.STATELESS)
                .addArgument(ParameterMetadata.of("input_char", DataType.UNICODE_CHAR, Direction.INPUT))
                .addArgument(ParameterMetadata.of("output_type", DataType.ENUM, Direction.OUTPUT))
                .build();

        TrainingSet trainingSet = TrainingSet.builder()
                .add(Parameter.of("input_char", "a"), Parameter.of("output_type", "VOWEL"))
                .add(Parameter.of("input_char", "b"), Parameter.of("output_type", "CONSONANT"))
                .build();


        org.palermo.circuit.process.Process.process(problem, trainingSet);

    }

}
