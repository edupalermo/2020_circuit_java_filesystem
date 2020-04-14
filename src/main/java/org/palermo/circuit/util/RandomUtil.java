package org.palermo.circuit.util;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {

    public static File generateUniqueTemporaryFile(File directory) {
        return generateUniqueFile(directory, "tmp");
    }

    public static File generateUniqueFile(File directory, String extension) {
        File file;
        do {
            file = new File(directory, generateString(8) + "." + extension);
        } while (file.exists());
        return file;
    }

    private static String generateString(int size) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < size; i++) {
            sb.append((char) ('a' + ThreadLocalRandom.current().nextInt('z' - 'a' + 1)));
        }

        return sb.toString();
    }
}
