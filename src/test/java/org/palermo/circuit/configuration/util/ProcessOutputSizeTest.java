package org.palermo.circuit.configuration.util;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProcessOutputSizeTest {

    @Test
    void testBitCalculation() {

        assertThrows(IllegalArgumentException.class, () ->ProcessOutputSize.numberOfBitsNeeded(0));
        assertThat(ProcessOutputSize.numberOfBitsNeeded(1)).isEqualTo(1);
        assertThat(ProcessOutputSize.numberOfBitsNeeded(2)).isEqualTo(1);
        assertThat(ProcessOutputSize.numberOfBitsNeeded(3)).isEqualTo(2);
        assertThat(ProcessOutputSize.numberOfBitsNeeded(4)).isEqualTo(2);
        assertThat(ProcessOutputSize.numberOfBitsNeeded(5)).isEqualTo(3);
        assertThat(ProcessOutputSize.numberOfBitsNeeded(6)).isEqualTo(3);
        assertThat(ProcessOutputSize.numberOfBitsNeeded(7)).isEqualTo(3);
        assertThat(ProcessOutputSize.numberOfBitsNeeded(8)).isEqualTo(3);
        assertThat(ProcessOutputSize.numberOfBitsNeeded(9)).isEqualTo(4);
        assertThat(ProcessOutputSize.numberOfBitsNeeded(10)).isEqualTo(4);
        assertThat(ProcessOutputSize.numberOfBitsNeeded(11)).isEqualTo(4);
        assertThat(ProcessOutputSize.numberOfBitsNeeded(12)).isEqualTo(4);
        assertThat(ProcessOutputSize.numberOfBitsNeeded(13)).isEqualTo(4);
        assertThat(ProcessOutputSize.numberOfBitsNeeded(14)).isEqualTo(4);
        assertThat(ProcessOutputSize.numberOfBitsNeeded(15)).isEqualTo(4);
        assertThat(ProcessOutputSize.numberOfBitsNeeded(16)).isEqualTo(4);
        assertThat(ProcessOutputSize.numberOfBitsNeeded(17)).isEqualTo(5);
    }
}
