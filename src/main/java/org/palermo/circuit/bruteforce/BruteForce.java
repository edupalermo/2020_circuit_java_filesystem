package org.palermo.circuit.bruteforce;

import org.palermo.circuit.Properties;
import org.palermo.circuit.bruteforce.port.InputPortEvaluator;
import org.palermo.circuit.bruteforce.port.OnePortEvaluator;
import org.palermo.circuit.bruteforce.port.PortEvaluator;
import org.palermo.circuit.bruteforce.port.ZeroPortEvaluator;
import org.palermo.circuit.circuit.CircuitResolver;
import org.palermo.circuit.configuration.ParameterMetadata;
import org.palermo.circuit.configuration.ParameterMetadata.Direction;
import org.palermo.circuit.configuration.Problem;
import org.palermo.circuit.configuration.Problem.ProblemType;
import org.palermo.circuit.util.IoUtils;
import org.palermo.circuit.util.PortUtil;
import org.palermo.circuit.util.RandomUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BruteForce {


    public static void process(Problem problem, File betterCircuit, File betterOutputConfiguration) {

        if (problem.getProblemType() != ProblemType.STATELESS) {
            throw new RuntimeException("Not implemented!");
        }

        RandomAccessFile circuit = null;

        try {
            File workingCircuit = RandomUtil.generateUniqueTemporaryFile(Properties.folderForTemporaryFiles);
            circuit = new RandomAccessFile(workingCircuit, "rw");

            // Should check if the new port was able to generate a unique output
            // at least for the training data
            PortUtil.writeZeroPort(circuit);

            //Context context = new Context(problem.getTrainingData());
            //context.evaluate(new ZeroPortEvaluator());

            // Should check if the new port was able to generate a better circuit
            // This is time to test all possible variation of the output
            CircuitResolver circuitResolver = new CircuitResolver();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        finally {
            IoUtils.closeQuitely(circuit);
        }


    }

    private List<PortEvaluator> generatePortEvaluator(Problem problem) {
        List<PortEvaluator> list = new ArrayList<>();

        list.add(new ZeroPortEvaluator());
        list.add(new OnePortEvaluator());

        for (Entry<String, ParameterMetadata> entry : problem.getParameterMetadataMap().entrySet()) {
            String parameterName = entry.getKey();
            if (entry.getValue().getDirection() == Direction.INPUT) {
                for (int i : problem.getRelevantBitsMap().get(parameterName)) {
                    list.add(new InputPortEvaluator(parameterName, i));
                }
            }
        }
        return list;
    }
}
