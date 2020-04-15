package org.palermo.circuit;

import org.palermo.circuit.bruteforce.BruteForce;
import org.palermo.circuit.circuit.CircuitResolver;
import org.palermo.circuit.configuration.Parameter;
import org.palermo.circuit.configuration.ParameterMetadata;
import org.palermo.circuit.configuration.ParameterMetadata.DataType;
import org.palermo.circuit.configuration.ParameterMetadata.Direction;
import org.palermo.circuit.configuration.Problem;
import org.palermo.circuit.configuration.Problem.ProblemType;
import org.palermo.circuit.configuration.Result;
import org.palermo.circuit.configuration.ResultFactory;
import org.palermo.circuit.stream.BitOutputStream;
import org.palermo.circuit.util.DataTypeUtil;
import org.palermo.circuit.util.IoUtils;
import org.palermo.circuit.util.RandomUtil;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

        BruteForce.process(problem);
    }

    private class VowelResultFactory implements ResultFactory {

        @Override
        public Result evaluate(File circuit,
                               File outputConfiguration,
                               List<Map<String, String>> trainingDataList,
                               Map<String, ParameterMetadata> parameterMetadataMap) {
            for (Map<String, String> trainingDataMap : trainingDataList) {
                evaluate(circuit, outputConfiguration, trainingDataMap, parameterMetadataMap);
            }

            return null;
        }

        private void evaluate(File circuit,
                              File outputConfiguration,
                              Map<String, String> trainingDataMap,
                              Map<String, ParameterMetadata> parameterMetadataMap) {
            File output = RandomUtil.generateUniqueFile(Properties.folderForTemporaryFiles, "output");
            File input = RandomUtil.generateUniqueFile(Properties.folderForTemporaryFiles, "output");

            writeInputData(input, trainingDataMap, parameterMetadataMap);
            CircuitResolver circuitResolver = new CircuitResolver(circuit, outputConfiguration);
            circuitResolver.resolve(input, output);



        }

        private void writeInputData(File input, Map<String, String> trainingDataMap, Map<String, ParameterMetadata> parameterMetadataMap) {
            BitOutputStream bitOutputStream = null;
            try {
                bitOutputStream = new BitOutputStream(input);
                for (Entry<String, String> entry : trainingDataMap.entrySet()) {
                    ParameterMetadata parameterMetadata = parameterMetadataMap.get(entry.getKey());
                    if (parameterMetadata.getDirection() == Direction.INPUT) {
                        for (int i = 0; i < DataTypeUtil.getSize(parameterMetadata); i++) {
                            bitOutputStream.writeBit(DataTypeUtil.getBit(parameterMetadata, entry.getValue(), i));
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            finally {
                IoUtils.closeQuitely(bitOutputStream);
            }

        }
    }

    private class VowelResult implements Result {

        public static final int INDEX_SIZE = 0;
        public static final int INDEX_DIFFERENCE = 1;


        @Override
        public boolean acceptable() {
            return false;
        }

        @Override
        public String asString() {
            return null;
        }

        @Override
        public boolean isBetterThan(Result result) {
            return false;
        }

        @Override
        public boolean equals(Result result) {
            return false;
        }
    }

}
