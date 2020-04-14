package org.palermo.circuit.configuration;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ResultFactory {

    Result evaluate(File circuit, File output, List<Map<String, String>> trainingDataList);
}
