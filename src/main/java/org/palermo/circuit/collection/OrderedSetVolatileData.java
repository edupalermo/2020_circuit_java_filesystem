package org.palermo.circuit.collection;

import org.palermo.circuit.Properties;
import org.palermo.circuit.util.RandomUtil;

import java.io.*;

public class OrderedSetVolatileData implements Closeable {

    private final RandomAccessFile indexRandomAccessFile;

    public OrderedSetVolatileData() {
        try {
            this.indexRandomAccessFile = new RandomAccessFile(RandomUtil.generateUniqueTemporaryFile(Properties.folderForTemporaryFiles), "rw");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean add(long id, DataGenerator dataGenerator) {

        try {
            long index = binarySearch(dataGenerator.generate(id), dataGenerator);

            if (index < 0) {
                insertIntoIndexFile(Math.abs(index) - 1, id);
            }

            return index < 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /*
    public long get(long index) {
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
    */


    private void insertIntoIndexFile(long position, long id) {
        try {
            long positionToSave = position * Long.BYTES;
            long it = indexRandomAccessFile.length() - Long.BYTES;
            while(it >= positionToSave) {
                indexRandomAccessFile.seek(it);
                indexRandomAccessFile.writeLong(indexRandomAccessFile.readLong());
                it = it - Long.BYTES;
            }
            indexRandomAccessFile.seek(positionToSave);
            indexRandomAccessFile.writeLong(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long binarySearch(File data, DataGenerator dataGenerator) throws IOException {
        return internalSearchOnIndexFile(0, indexRandomAccessFile.length() / Long.BYTES, data, dataGenerator);
    }

    private long internalSearchOnIndexFile(long fromIndex, long toIndex, File data, DataGenerator dataGenerator) {
        try {
            long low = fromIndex;
            long high = toIndex - 1;

            while (low <= high) {
                long mid = (low + high) >>> 1;

                indexRandomAccessFile.seek(mid * Long.BYTES);
                long idToBeCompared = indexRandomAccessFile.readLong();
                int cmp = compare(dataGenerator.generate(idToBeCompared), data);

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

    private int compare(File file1, File file2) {

        if (file1.length() != file2.length()) {
            throw new RuntimeException("Comparing different things!");
        }

        if (file1.length() == 0) {
            throw new RuntimeException("Zero size is not allowed!");
        }

        int result = 0;

        try {
            FileInputStream input1 = new FileInputStream(file1);
            FileInputStream input2 = new FileInputStream(file2);

            int lastByte;

            do {
                result = Integer.compare(lastByte = input1.read(), input2.read());

            } while((lastByte != -1) && (result == 0));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void close() {
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

    public static interface DataGenerator {
        File generate(long id);
    }
}
