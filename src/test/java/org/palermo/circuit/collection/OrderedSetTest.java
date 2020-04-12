package org.palermo.circuit.collection;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class OrderedSetTest {

    @Test
    void generalTest() {

        File dataFile = new File("data.dat");
        File indexFile = new File("index.dat");

        OrderedSet orderedSet = null;
        try {
            orderedSet = new OrderedSet(dataFile, indexFile, Integer.BYTES);

            assertThat(orderedSet.add(toByteArray(1))).isTrue();
            assertThat(orderedSet.add(toByteArray(2))).isTrue();
            assertThat(orderedSet.add(toByteArray(3))).isTrue();
            assertThat(orderedSet.add(toByteArray(4))).isTrue();
            assertThat(orderedSet.add(toByteArray(5))).isTrue();
            assertThat(orderedSet.add(toByteArray(0))).isTrue();
            assertThat(orderedSet.add(toByteArray(10))).isTrue();
            assertThat(orderedSet.add(toByteArray(14))).isTrue();
            assertThat(orderedSet.add(toByteArray(13))).isTrue();
            assertThat(orderedSet.add(toByteArray(9))).isTrue();
            assertThat(orderedSet.add(toByteArray(8))).isTrue();
            assertThat(orderedSet.add(toByteArray(1))).isFalse();
            assertThat(orderedSet.add(toByteArray(14))).isFalse();

        } finally {
            if (orderedSet != null) {
                orderedSet.close();
            }

            dataFile.delete();
            indexFile.delete();
        }
    }

    private byte[] toByteArray(int input) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeInt(input);
            dos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
