package org.palermo.circuit;

import org.palermo.circuit.configuration.Parameter;
import org.palermo.circuit.configuration.ParameterMetadata;
import org.palermo.circuit.configuration.ParameterMetadata.DataType;
import org.palermo.circuit.configuration.ParameterMetadata.Direction;
import org.palermo.circuit.configuration.Problem;
import org.palermo.circuit.configuration.Problem.ProblemType;

public class Main {

    public static void main(String[] args) {

        Problem problem = Problem.builder()
                .type(ProblemType.STATELESS)
                .addArgument(ParameterMetadata.of("input_char", DataType.UNICODE_CHAR, Direction.INPUT))
                .addArgument(ParameterMetadata.of("output_type", DataType.ENUM, Direction.OUTPUT))
                .addTrainingData(Parameter.of("input_char", "a"), Parameter.of("output_type", "VOWEL"))
                .addTrainingData(Parameter.of("input_char", "b"), Parameter.of("output_type", "CONSONANT"))
                .addTrainingData(Parameter.of("input_char", "c"), Parameter.of("output_type", "CONSONANT"))
                .addTrainingData(Parameter.of("input_char", "d"), Parameter.of("output_type", "CONSONANT"))
                .addTrainingData(Parameter.of("input_char", "A"), Parameter.of("output_type", "VOWEL"))
                .addTrainingData(Parameter.of("input_char", "B"), Parameter.of("output_type", "CONSONANT"))
                .addTrainingData(Parameter.of("input_char", "C"), Parameter.of("output_type", "CONSONANT"))
                .addTrainingData(Parameter.of("input_char", "D"), Parameter.of("output_type", "CONSONANT"))
                .addTrainingData(Parameter.of("input_char", "E"), Parameter.of("output_type", "VOWEL"))
                .addTrainingData(Parameter.of("input_char", "F"), Parameter.of("output_type", "CONSONANT"))
                .addTrainingData(Parameter.of("input_char", "G"), Parameter.of("output_type", "CONSONANT"))
                .addTrainingData(Parameter.of("input_char", "."), Parameter.of("output_type", "SYMBOL"))
                .build();



    }

}
