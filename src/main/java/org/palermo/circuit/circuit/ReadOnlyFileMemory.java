package org.palermo.circuit.circuit;

import org.palermo.circuit.Properties;
import org.palermo.circuit.util.RandomUtil;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ReadOnlyFileMemory implements Closeable {

    private final RandomAccessFile randomAccessFile;

    public ReadOnlyFileMemory(File memoryFile) {
        try {
            this.randomAccessFile = new RandomAccessFile(memoryFile, "r");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getBit(long offset) {
        try {
            randomAccessFile.seek(offset / 8);
            int info = randomAccessFile.read();

            return (info >>> (7 - (offset % 8))) & 0x01;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        closeQuietly(randomAccessFile);
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
