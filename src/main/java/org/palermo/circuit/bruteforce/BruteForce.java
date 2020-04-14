package org.palermo.circuit.bruteforce;

import org.palermo.circuit.bruteforce.port.InputPortEvaluator;
import org.palermo.circuit.bruteforce.port.OnePortEvaluator;
import org.palermo.circuit.bruteforce.port.PortEvaluator;
import org.palermo.circuit.bruteforce.port.ZeroPortEvaluator;
import org.palermo.circuit.configuration.ParameterMetadata;
import org.palermo.circuit.configuration.ParameterMetadata.Direction;
import org.palermo.circuit.configuration.Problem;
import org.palermo.circuit.configuration.Problem.ProblemType;
import org.palermo.circuit.util.IoUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BruteForce {


    public static void process(Problem problem) {

        if (problem.getProblemType() != ProblemType.STATELESS) {
            throw new RuntimeException("Not implemented!");
        }


        int blockSize = (int) Math.ceil(problem.getTrainingData().size() / 8);

        Context context = new Context(problem.getTrainingData());

        Comparable result = context.add();


//        context.evaluate(new ZeroPortEvaluator(), problem.getTrainingData());
//        context.evaluate(new OnePortEvaluator());

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
