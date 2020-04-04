package org.palermo.circuit.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class IoUtils {

    public static void closeQuitely(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }
}
