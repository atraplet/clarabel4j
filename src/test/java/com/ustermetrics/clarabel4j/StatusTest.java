package com.ustermetrics.clarabel4j;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
    void valueOfDoesNotThrow(int status) {
        assertDoesNotThrow(() -> Status.valueOf(status));
    }

    @Test
    void valueOf100ThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Status.valueOf(100));

        assertEquals("Unknown status 100", exception.getMessage());
    }

}
