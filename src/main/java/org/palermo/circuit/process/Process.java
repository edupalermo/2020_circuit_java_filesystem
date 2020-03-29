package org.palermo.circuit.process;

import org.palermo.circuit.configuration.ParameterMetadata;
import org.palermo.circuit.configuration.Problem;
import org.palermo.circuit.configuration.Problem.ProblemType;
import org.palermo.circuit.configuration.TrainingSet;
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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Process {

    private static final File DIRECTORY = new File("C:\\Temp\\circuit");

    private static final String FILENAME_MASK = "bit_%d.dat";

    public static void process(Problem problem, TrainingSet trainingSet) {

        if (problem.getProblemType() == ProblemType.STREAM) {
            throw new RuntimeException("Not implemented!");
        }

        generateFixed(trainingSet);

        int current = 2;

        for (String parameterName : problem.getParameterMetadata().keySet()) {
            ParameterMetadata parameterMetadata = problem.getParameterMetadata().get(parameterName);

            String filename = String.format(FILENAME_MASK, current);

            for (int i = 0; i < DataTypeUtil.getSize(parameterMetadata); i++) {
                BitOutputStream test = new BitOutputStream(new File(DIRECTORY, filename));

                for (Map<String, String> map : trainingSet.getList()) { // The amount of cycles
                    test.writeBit(DataTypeUtil.getBit(parameterMetadata, map.get(parameterName) , i));
                }

                test.close();

                if (hasNewInformation(filename)) {
                    current++;
                }
                else {
                    if (!(new File(DIRECTORY, filename)).delete()) {
                        throw new RuntimeException("Fail to delete temporary file.");
                    }
                }

            }
        }
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

                if (n1 == -1 || n2 == -1) return n1 == n2;

                buf1.flip();
                buf2.flip();

                for (int i = 0; i < Math.min(n1, n2); i++)
                    if (buf1.get() != buf2.get())
                        return false;

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

    private static void generateFixed(TrainingSet trainingSet) {
        BitOutputStream zero = new BitOutputStream(new File(DIRECTORY, "bit_0.dat"));
        BitOutputStream one = new BitOutputStream(new File(DIRECTORY, "bit_1.dat"));
        for (int i = 0; i < trainingSet.getList().size(); i++) {
            zero.writeBit(0);
            one.writeBit(1);
        }
        zero.close();
        one.close();
    }

}
