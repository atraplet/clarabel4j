package com.ustermetrics.clarabel4j;

import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZeroConeTest {

    private static ZeroCone cone;

    @BeforeAll
    static void setUp() {
        cone = new ZeroCone(2);
    }

    @Test
    void getTagDoesNotThrow() {
        assertDoesNotThrow(cone::getTag);
    }

    @Test
    void getDimensionReturnsCorrectDimension() {
        assertEquals(2, cone.getDimension());
    }

    @Test
    void getNReturnsN() {
        assertEquals(2, cone.getN());
    }

    @Test
    void newZeroConeWithZeroNThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> new ZeroCone(0));

        assertEquals("n must be positive", exception.getMessage());
    }

}
