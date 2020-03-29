package org.palermo.circuit.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TrainingSet {

    private final List<Map<String, String>> list;
    private final Problem problem;

    private TrainingSet(Problem problem, List<Map<String, String>> list) {
        this.list = Collections.unmodifiableList(list);
        this.problem = problem;
    }

    public static TrainingSetBuilder builder() {
        return new TrainingSetBuilder();
    }

    public List<Map<String, String>> getList() {
        return list;
    }

    public Problem getProblem() {
        return problem;
    }

    public static class TrainingSetBuilder {

        private List<Map<String, String>> list = new ArrayList<>();
        private Problem problem;

        public TrainingSetBuilder add(Parameter... parameters) {
            Map<String, String> map = new TreeMap<>();
            for (Parameter parameter : parameters) {
                map.put(parameter.getName(), parameter.getValue());
            }
            list.add(Collections.unmodifiableMap(map));
            return this;
        }

        public TrainingSetBuilder poblem(Problem problem) {
            this.problem = problem;
            return this;
        }

        public TrainingSet build() {
            return new TrainingSet(this.problem, this.list);
        }
    }
}
