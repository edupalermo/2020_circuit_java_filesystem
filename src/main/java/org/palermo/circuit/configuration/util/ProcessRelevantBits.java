package org.palermo.circuit.configuration.util;

import org.palermo.circuit.configuration.ParameterMetadata;
import org.palermo.circuit.configuration.ParameterMetadata.Direction;
import org.palermo.circuit.configuration.Problem.ProblemType;
import org.palermo.circuit.stream.BitOutputStream;
import org.palermo.circuit.util.DataTypeUtil;
import org.palermo.circuit.util.IoUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessRelevantBits {

    private static final File DIRECTORY = new File("C:\\Temp\\circuit");
    private static final String FILENAME_MASK = "bit_%d.dat";

    public static Map<String, Set<Integer>> process(ProblemType problemType,
                               Map<String, ParameterMetadata> parameterMetadataMap,
                               List<Map<String, String>> trainingData) {

        Map<String, Set<Integer>> relevantBits = new TreeMap<>();

        if (problemType == ProblemType.STREAM) {
            throw new RuntimeException("Not implemented!");
        }

        generateFixed(trainingData);

        int current = 2;

        // Evaluate each Input parameter
        for (Entry<String, ParameterMetadata> entry : parameterMetadataMap.entrySet()) {
            String parameterName = entry.getKey();
            ParameterMetadata parameterMetadata = entry.getValue();

            if (parameterMetadata.getDirection() == Direction.OUTPUT) {
                continue;
            }

            for (int i = 0; i < DataTypeUtil.getSize(parameterMetadata); i++) {
                String filename = createFilename(current);
                BitOutputStream test = new BitOutputStream(new File(DIRECTORY, filename));

                // for (int notImportant = 0; notImportant < trainingData.size(); notImportant++) { // The amount of cycles
                for (Map<String, String> dataMap : trainingData) {
                    test.writeBit(DataTypeUtil.getBit(parameterMetadata, dataMap.get(parameterName), i));
                }

                test.close();

                if (hasNewInformation(filename)) {
                    addRelevantBit(relevantBits, parameterName, i);
                    current++;
                } else {
                    if (!(new File(DIRECTORY, filename)).delete()) {
                        throw new RuntimeException("Fail to delete temporary file.");
                    }
                }

            }
        }

        for (int i = 0; i < current - 1; i++) {
            if (!(new File(DIRECTORY, createFilename(i))).delete()) {
                throw new RuntimeException("Fail to delete temporary file.");
            }
        }

        return relevantBits;
    }

    private static void addRelevantBit(Map<String, Set<Integer>> relevantBits, String parameterName, int index) {
        Set<Integer> set = relevantBits.get(parameterName);
        if (set == null) {
            set = new TreeSet<>();
            set.add(index);
            relevantBits.put(parameterName, set);
        }
        set.add(index);
    }

    private static String createFilename(int index) {
        return String.format(FILENAME_MASK, index);
    }

    private static boolean hasNewInformation(String filename) {
        boolean newInformation = true;
        for (int i = 0; i < getIndex(filename); i++) {
            if (isEqual(filename, String.format(FILENAME_MASK, i))) {
                newInformation = false;
                break;
            }
        }
        return newInformation;
    }

    private static boolean isEqual(String filename1, String filename2) {
        try {
            return isEqual(new FileInputStream(new File(DIRECTORY, filename1)),
                    new FileInputStream(new File(DIRECTORY, filename2)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isEqual(InputStream i1, InputStream i2) {

        ReadableByteChannel ch1 = Channels.newChannel(i1);
        ReadableByteChannel ch2 = Channels.newChannel(i2);

        ByteBuffer buf1 = ByteBuffer.allocateDirect(1024);
        ByteBuffer buf2 = ByteBuffer.allocateDirect(1024);

        try {
            while (true) {

                int n1 = ch1.read(buf1);
                int n2 = ch2.read(buf2);

                if (n1 == -1 || n2 == -1) {
                    return n1 == n2;
                }

                buf1.flip();
                buf2.flip();

                for (int i = 0; i < Math.min(n1, n2); i++) {
                    if (buf1.get() != buf2.get()) {
                        return false;
                    }
                }

                buf1.compact();
                buf2.compact();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtils.closeQuitely(i1);
            IoUtils.closeQuitely(i2);
        }
    }

    private static int getIndex(String filename) {
        Pattern pattern = Pattern.compile("^bit_(\\d+)\\.dat$");
        Matcher matcher = pattern.matcher(filename);
        if (!matcher.find()) {
            throw new RuntimeException("Wrong filename pattern.");
        }
        return Integer.parseInt(matcher.group(1));
    }

    private static void generateFixed(List<Map<String, String>> trainingData) {
        BitOutputStream zero = null;
        BitOutputStream one = null;

        try {
            zero = new BitOutputStream(new File(DIRECTORY, createFilename(0)));
            one = new BitOutputStream(new File(DIRECTORY, createFilename(1)));

            for (int i = 0; i < trainingData.size(); i++) {
                zero.writeBit(0);
                one.writeBit(1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IoUtils.closeQuitely(zero);
            IoUtils.closeQuitely(one);
        }
    }

}
