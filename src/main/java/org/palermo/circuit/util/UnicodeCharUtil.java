package org.palermo.circuit.util;

public class UnicodeCharUtil {

    public static int getBit(String value, int position) {
        if (position < 0 || position >= 16) {
            throw new IllegalArgumentException("Illegal position value " + position);
        }
        if (value == null ||value.length() > 1) {
            throw new IllegalArgumentException("Illegal value " + value);
        }

        return (convert(value) >> (15 - position)) & 0x01;
    }

    private static char convert(String value) {
        return value.charAt(0);
    }

}
