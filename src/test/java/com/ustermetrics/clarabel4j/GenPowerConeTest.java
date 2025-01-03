package com.ustermetrics.clarabel4j;

import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenPowerConeTest {

    private static GenPowerCone cone;

    @BeforeAll
    static void setUp() {
        cone = new GenPowerCone(new double[]{0.4, 0.6}, 2);
    }

    @Test
    void getTagDoesNotThrow() {
        assertDoesNotThrow(cone::getTag);
    }

    @Test
    void getDimensionReturnsCorrectDimension() {
        assertEquals(4, cone.getDimension());
    }

    @Test
    void getAReturnsA() {
        assertArrayEquals(new double[]{0.4, 0.6}, cone.getA(), 1e-8);
    }

    @Test
    void getNReturnsN() {
        assertEquals(2, cone.getN());
    }

    @Test
    void newGenPowerConeWithZeroLengthAThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class,
                () -> new GenPowerCone(new double[0], 2));

        assertEquals("length of a must be positive", exception.getMessage());
    }

    @Test
    void newGenPowerConeWithZeroElementInAThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class,
                () -> new GenPowerCone(new double[]{0., 0.6}, 2));

        assertEquals("all entries of a must be in (0, 1)", exception.getMessage());
    }

    @Test
    void newGenPowerConeWithElementsInANotSummingUpToOneThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class,
                () -> new GenPowerCone(new double[]{0.1, 0.6}, 2));

        assertEquals("the sum of all entries of a must be equal to one", exception.getMessage());
    }

    @Test
    void newGenPowerConeWithZeroNThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class,
                () -> new GenPowerCone(new double[]{0.4, 0.6}, 0));

        assertEquals("n must be positive", exception.getMessage());
    }

}
