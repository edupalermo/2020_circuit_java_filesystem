package org.palermo.circuit.util;

import org.palermo.circuit.bruteforce.port.PortEvaluator;
import org.palermo.circuit.circuit.FileMemory;
import org.palermo.circuit.circuit.ReadOnlyFileMemory;

import java.io.IOException;
import java.io.RandomAccessFile;

public class PortUtil {
    private static final int TYPE_HARDCODE = 0x00;
    private static final int TYPE_INPUT = 0x01;
    private static final int TYPE_NAND = 0x02;
    private static final int TYPE_MEMORY = 0x03;

    private final static int CIRCUIT_RECORD_BYTE_SIZE = 17;

    public static int evaluate(RandomAccessFile circuit, FileMemory memory, ReadOnlyFileMemory input) {
        try {
            int answer = 0;
            switch(circuit.read()) {
                case TYPE_HARDCODE:
                    answer =  (int) circuit.readLong();
                    circuit.skipBytes(8);
                    break;
                case TYPE_INPUT:
                    answer = input.getBit(circuit.readLong());
                    circuit.skipBytes(8);
                    break;
                case TYPE_NAND:
                    answer = 0x01 & ~(memory.getBit(circuit.readLong()) & memory.getBit(circuit.readLong()));
                    break;
                default:
                    throw new RuntimeException("Not implemented!");
            }
            return answer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeZeroPort(RandomAccessFile circuit) {
        try {
            circuit.writeByte(TYPE_HARDCODE);
            circuit.writeLong(0L);
            circuit.writeLong(0L);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeOnePort(RandomAccessFile circuit) {
        try {
            circuit.writeByte(TYPE_HARDCODE);
            circuit.writeLong(1L);
            circuit.writeLong(0L);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeInputPort(RandomAccessFile circuit, long index) {
        try {
            circuit.writeByte(TYPE_INPUT);
            circuit.writeLong(index);
            circuit.writeLong(0L);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeNandPort(RandomAccessFile circuit, long index1, long index2) {
        try {
            circuit.writeByte(TYPE_INPUT);
            circuit.writeLong(index1);
            circuit.writeLong(index2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
