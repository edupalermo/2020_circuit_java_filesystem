package org.palermo.circuit.circuit;

import org.palermo.circuit.bruteforce.port.PortEvaluator;
import org.palermo.circuit.stream.BitOutputStream;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class CircuitResolver implements Closeable {

    private final RandomAccessFile circuit;
    private final RandomAccessFile outputConfiguration;

    private final static int CIRCUIT_RECORD_BYTE_SIZE = 17;

    public CircuitResolver(File circuit, File outputConfiguration) {
        try {
            this.circuit = new RandomAccessFile(circuit, "r");
            this.outputConfiguration = new RandomAccessFile(outputConfiguration, "r");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void resolve(File inputFile, File outputFile) {
        FileMemory memory = null;
        ReadOnlyFileMemory input = null;
        try {
            long numberOfPorts = this.circuit.length() / CIRCUIT_RECORD_BYTE_SIZE;

            memory = new FileMemory(numberOfPorts);
            input = new ReadOnlyFileMemory(inputFile);

            for (long i = 0; i < numberOfPorts; i++) {
                memory.setBit(i, resolvePort(i, memory, input));
            }

            writeOutputFile(memory, outputFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            closeQuietly(memory);
            closeQuietly(input);
        }
    }

    private void writeOutputFile(FileMemory memory, File outputFile) {
        BitOutputStream bitOutputStream = null;
        try {
            bitOutputStream = new BitOutputStream(outputFile);
            for (long i = 0; i < outputConfiguration.length() / Long.BYTES; i++) {
                bitOutputStream.writeBit(memory.getBit(outputConfiguration.readLong()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (bitOutputStream != null) {
                bitOutputStream.close();
            }
        }
    }

    private int resolvePort(long port, FileMemory memory, ReadOnlyFileMemory input) {
        try {
            circuit.seek(port * CIRCUIT_RECORD_BYTE_SIZE);
            switch(circuit.read()) {
            case PortEvaluator.TYPE_ZERO:
                return 0;
            case PortEvaluator.TYPE_ONE:
                return 1;
            case PortEvaluator.TYPE_INPUT:
                return input.getBit(circuit.readLong());
            case PortEvaluator.TYPE_NAND:
                return 0x01 & ~(memory.getBit(circuit.readLong()) & memory.getBit(circuit.readLong()));
            default:
                throw new RuntimeException("Not implemented!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        closeQuietly(circuit);
        closeQuietly(outputConfiguration);
    }

    private void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
    }
}
