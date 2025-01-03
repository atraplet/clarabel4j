package com.ustermetrics.clarabel4j;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class DirectSolveMethodTest {

    @ParameterizedTest
    @ValueSource(ints = {0})
    void valueOfDoesNotThrow(int status) {
        assertDoesNotThrow(() -> DirectSolveMethod.valueOf(status));
    }

    @Test
    void valueOf100ThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> DirectSolveMethod.valueOf(100));

        assertEquals("Unknown direct solve method 100", exception.getMessage());
    }

}
