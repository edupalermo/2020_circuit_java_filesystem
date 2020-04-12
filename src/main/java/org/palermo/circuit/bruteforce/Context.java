package org.palermo.circuit.bruteforce;

import org.palermo.circuit.bruteforce.port.PortEvaluator;
import org.palermo.circuit.util.IoUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;

public class Context {

    private static final File FOLDER = new File("");

    private final RandomAccessFile circuitFile;
    private final RandomAccessFile indexFile;
    private final RandomAccessFile outputDataFile;

    private final List<Map<String, String>> trainingDataList;

    private long numberOfPorts = 0;

    public Context(List<Map<String, String>> trainingDataList) {
        try {
            circuitFile = new RandomAccessFile(new File(FOLDER, "circuit.dat"), "rw");
            indexFile = new RandomAccessFile(new File(FOLDER, "index.dat"), "rw");
            outputDataFile = new RandomAccessFile(new File(FOLDER, "output_data.dat"), "rw");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.trainingDataList = trainingDataList;
    }

    public void evaluate(PortEvaluator portEvaluator) {

        try {
            populateOutputs(portEvaluator);
            long position = searchOnIndexFile();

            if (position < 0) { // New Information
                insertIntoIndexFile(Math.abs(position) , numberOfPorts);
                circuitFile.seek(circuitFile.length());
                circuitFile.write(portEvaluator.getDescription());
                numberOfPorts = numberOfPorts + 1;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertIntoIndexFile(long position, long value) {
        try {
            long positionToSave = indexFile.length() - Long.BYTES;
            long it = positionToSave;
            while(it >= position) {
                indexFile.seek(it);
                indexFile.writeLong(indexFile.readLong());
                it = it - Long.BYTES;
            }
            indexFile.seek(positionToSave);
            indexFile.writeLong(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long searchOnIndexFile() {
        try {
            return internalSearchOnIndexFile(0, indexFile.length() / Long.BYTES);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private long internalSearchOnIndexFile(long fromIndex, long toIndex) {
        try {
            long low = fromIndex;
            long high = toIndex - 1;

            while (low <= high) {
                long mid = (low + high) >>> 1;

                indexFile.seek(mid * Long.BYTES);
                long translatedIndex = indexFile.readLong();

                int cmp = compare(translatedIndex, new byte[] {}); //TODO Fix me

                if (cmp < 0)
                    low = mid + 1;
                else if (cmp > 0)
                    high = mid - 1;
                else
                    return mid; // key found
            }
            return -(low + 1);  // key not found.
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int compare(long key, byte[] data2) {
        int result = 0;

        try {
            for (int i = 0; i < getOutputSizeInBytes() && result == 0; i++) {
                // The pointer (or middle)
                outputDataFile.seek(key + i);
                int b1 = outputDataFile.read();

                // The new stuff
                outputDataFile.seek(outputDataFile.length() - getOutputSizeInBytes() + i);
                int b2 = outputDataFile.read();

                result = Integer.compare(b1, b2);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private void populateOutputs(PortEvaluator portEvaluator) {
        try {
            circuitFile.seek(numberOfPorts * getOutputSizeInBytes());
            //Initialize empty data
            for (int i = 0; i < getOutputSizeInBytes(); i++) {
                circuitFile.write(0x00);
            }

            circuitFile.seek(numberOfPorts * getOutputSizeInBytes());

            for (int i = 0; i < trainingDataList.size(); i++) {
                Map<String, String> parameters = trainingDataList.get(i);
                setBit(outputDataFile, i, portEvaluator.getValue(parameters));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setBit(RandomAccessFile randomAccessFile, int bit, int value) {
        if (value == 0) {
            return;
        }
        try {
            long position = numberOfPorts * getOutputSizeInBytes() + (bit / 8);
            // Read information
            randomAccessFile.seek(position);
            int info = randomAccessFile.read();
            //Update information
            randomAccessFile.seek(position);
            int shift = 7 - (bit % 8);
            randomAccessFile.write(info | (0x01 << shift));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long getOutputSizeInBytes() {
        return (long) Math.ceil((double) trainingDataList.size() / 8d);
    }

    public void close() {
        IoUtils.closeQuitely(circuitFile);
        IoUtils.closeQuitely(indexFile);
        IoUtils.closeQuitely(outputDataFile);
    }

}
