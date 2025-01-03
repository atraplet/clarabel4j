package com.ustermetrics.clarabel4j;

import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NonnegativeConeTest {

    private static NonnegativeCone cone;

    @BeforeAll
    static void setUp() {
        cone = new NonnegativeCone(2);
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
    void newNonnegativeConeWithZeroNThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> new NonnegativeCone(0));

        assertEquals("n must be positive", exception.getMessage());
    }

}
