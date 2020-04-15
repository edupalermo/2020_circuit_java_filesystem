package org.palermo.circuit.configuration;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ResultFactory {

    Result evaluate(File circuit,
                    File outputConfiguration,
                    List<Map<String, String>> trainingDataList,
                    Map<String, ParameterMetadata> parameterMetadataMap);
}
