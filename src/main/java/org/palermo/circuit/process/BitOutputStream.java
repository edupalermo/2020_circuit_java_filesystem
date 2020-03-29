package org.palermo.circuit.process;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitOutputStream implements Closeable {
    private static final int BYTE_SIZE = 8;  // digits per byte
    private FileOutputStream output;
    private int buffer;     // a buffer used to build up next set of digits
    private int numDigits;  // how many digits are currently in the buffer

    public BitOutputStream(String file) {
        this(new File(file));
    }

    public BitOutputStream(File file) {
        try {
            output = new FileOutputStream(file);
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
        buffer = numDigits = 0;
    }

    // post: writes given bit to output
    public void writeBit(int bit) {
        if (bit < 0 || bit > 1)
            throw new IllegalArgumentException("Illegal bit: " + bit);
        buffer += bit << numDigits;
        numDigits++;
        if (numDigits == BYTE_SIZE)
            flush();
    }

    // post: Flushes the buffer.  If numDigits < BYTE_SIZE, this will
    //       effectively pad the output with extra 0's, so this should
    //       be called only when numDigits == BYTE_SIZE or when we are
    //       closing the output.
    private void flush() {
        try {
            output.write(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
        buffer = 0x0;
        numDigits = 0;
    }

    // post: output is closed
    public void close() {
        if (numDigits > 0)
            flush();
        try {
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    // included to ensure that the stream is closed
    protected void finalize() {
        close();
    }
}
