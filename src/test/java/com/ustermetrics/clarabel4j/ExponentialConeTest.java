package com.ustermetrics.clarabel4j;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ExponentialConeTest {

    private static Cone cone;

    @BeforeAll
    static void setUp() {
        cone = new ExponentialCone();
    }

    @Test
    void getTagDoesNotThrow() {
        assertDoesNotThrow(cone::getTag);
    }

    @Test
    void getDimensionReturnsCorrectDimension() {
        assertEquals(3, cone.getDimension());
    }

}
