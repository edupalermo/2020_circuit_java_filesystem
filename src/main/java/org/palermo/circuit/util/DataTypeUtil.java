package org.palermo.circuit.util;

import org.palermo.circuit.configuration.ParameterMetadata;

public class DataTypeUtil {

    public static int getSize(ParameterMetadata parameterMetadata) {
        switch(parameterMetadata.getDataType()) {
        case UNICODE_CHAR:
            return 16;
        default:
            throw new RuntimeException("Not implemented for " + parameterMetadata.getDataType().name());
        }
    }

    public static int getBit(ParameterMetadata parameterMetadata, String value, int position) {
        if (position < 0) {
            throw new IllegalArgumentException("Negative position is invalid " + position);
        }
        switch(parameterMetadata.getDataType()) {
        case UNICODE_CHAR:
            return UnicodeCharUtil.getBit(value, position);
        default:
            throw new RuntimeException("Not implemented!");
        }
    }

}
