package org.palermo.circuit.collection;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class OrderedSet implements Closeable {

    private final RandomAccessFile dataRandomAccessFile;
    private final RandomAccessFile indexRandomAccessFile;
    private final int dataSize;

    public OrderedSet(File dataFile, File indexFile, int dataSize) {
        try {
            this.dataRandomAccessFile = new RandomAccessFile(dataFile, "rw");
            this.indexRandomAccessFile = new RandomAccessFile(indexFile, "rw");
            this.dataSize = dataSize;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean add(byte[] data) {

        if (data == null || data.length != dataSize) {
            throw new RuntimeException("Invalid data!");
        }

        try {
            long index = binarySearch(data);

            if (index < 0) {
                long offset = dataRandomAccessFile.length();
                insertIntoIndexFile(Math.abs(index) - 1, offset);
                dataRandomAccessFile.seek(offset);
                dataRandomAccessFile.write(data);
            }

            return index < 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public byte[] get(long index) {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException("Invalid index: " + index);
        }
        try {
            byte[] data = new byte[this.dataSize];
            dataRandomAccessFile.seek(index);
            dataRandomAccessFile.readFully(data);
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void insertIntoIndexFile(long position, long value) {
        try {
            long positionToSave = position * Long.BYTES;
            long it = indexRandomAccessFile.length() - Long.BYTES;
            while(it >= positionToSave) {
                indexRandomAccessFile.seek(it);
                indexRandomAccessFile.writeLong(indexRandomAccessFile.readLong());
                it = it - Long.BYTES;
            }
            indexRandomAccessFile.seek(positionToSave);
            indexRandomAccessFile.writeLong(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long binarySearch(byte[] data) throws IOException {
        return internalSearchOnIndexFile(0, indexRandomAccessFile.length() / Long.BYTES, data);
    }

    private long internalSearchOnIndexFile(long fromIndex, long toIndex, byte[] data) {
        try {
            long low = fromIndex;
            long high = toIndex - 1;

            while (low <= high) {
                long mid = (low + high) >>> 1;

                indexRandomAccessFile.seek(mid * Long.BYTES);
                int cmp = compare(get(indexRandomAccessFile.readLong()), data);

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

    private int compare(byte[] data1, byte[] data2) {
        if (data1.length != data2.length) {
            throw new RuntimeException("Data of different size");
        }

        int result = 0;
        for (int i = 0; i < data1.length && result == 0; i++) {
            result = Byte.compare(data1[i], data2[i]);
        }
        return result;
    }

    @Override
    public void close() {
        closeQuietly(this.dataRandomAccessFile);
        closeQuietly(this.indexRandomAccessFile);
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
