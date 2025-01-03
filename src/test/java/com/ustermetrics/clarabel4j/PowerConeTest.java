package com.ustermetrics.clarabel4j;

import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PowerConeTest {

    private static PowerCone cone;

    @BeforeAll
    static void setUp() {
        cone = new PowerCone(0.5);
    }

    @Test
    void getTagDoesNotThrow() {
        assertDoesNotThrow(cone::getTag);
    }

    @Test
    void getDimensionReturnsCorrectDimension() {
        assertEquals(3, cone.getDimension());
    }

    @Test
    void getAReturnsA() {
        assertEquals(0.5, cone.getA(), 1e-8);
    }

    @Test
    void newPowerConeWithZeroAThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> new PowerCone(0.));

        assertEquals("a must be in (0, 1)", exception.getMessage());
    }

}
