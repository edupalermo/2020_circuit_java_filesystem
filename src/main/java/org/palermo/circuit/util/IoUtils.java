package org.palermo.circuit.util;

import java.io.IOException;
import java.io.InputStream;

public class IoUtils {

    public static void closeQuitely(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }
}
