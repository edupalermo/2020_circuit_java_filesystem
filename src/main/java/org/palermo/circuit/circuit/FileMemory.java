package org.palermo.circuit.circuit;

import org.palermo.circuit.Properties;
import org.palermo.circuit.util.RandomUtil;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileMemory implements Closeable {

    private final RandomAccessFile randomAccessFile;
    private final long size;

    public FileMemory(long size) {
        try {
            this.size = size;
            this.randomAccessFile = new RandomAccessFile(RandomUtil.generateUniqueTemporaryFile(Properties.folderForTemporaryFiles), "rw");

            for(int i = 0 ; i < Math.ceil(size / 8d); i++) {
                randomAccessFile.write(0x00);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setBit(long offset, int value) {
        if (offset < 0 && offset >= size) {
            throw new ArrayIndexOutOfBoundsException("Invalid offset " + offset);
        }
        if (value != 0 && value != 1) {
            throw new RuntimeException("Invalid value " + value);
        }
        try {
            randomAccessFile.seek(offset / 8);
            int info = randomAccessFile.read();

            if (value == 1) {
                info |= 1 << (7 - (offset % 8));
            }
            else {
                info &= 1 << ~(7 - (offset % 8));
            }
            randomAccessFile.seek(offset / 8);
            randomAccessFile.write(info);

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
