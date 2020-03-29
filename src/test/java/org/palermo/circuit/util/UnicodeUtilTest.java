package org.palermo.circuit.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class UnicodeUtilTest {

    @Test
    void test() {
        System.out.println((int) 'a');

        for (int i = 0; i < 16; i++) {
            System.out.println(i + " - " + UnicodeCharUtil.getBit("a", i));
        }
    }

    @Test
    void testOutOfBoundParameters() {
        assertThrows(IllegalArgumentException.class, () -> UnicodeCharUtil.getBit("a", -1));
        assertThrows(IllegalArgumentException.class, () -> UnicodeCharUtil.getBit("a", 16));
    }

    @Test
    void testIllegalParameters() {
        assertThrows(IllegalArgumentException.class, () -> UnicodeCharUtil.getBit(null, 0));
        assertThrows(IllegalArgumentException.class, () -> UnicodeCharUtil.getBit("ab", 1));
    }

}
