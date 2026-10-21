// SPDX-FileCopyrightText: Uster Metrics GmbH
// SPDX-License-Identifier: Apache-2.0
package com.ustermetrics.clarabel4j;

import static org.junit.jupiter.api.Assertions.*;

import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SecondOrderConeTest {

    private static SecondOrderCone cone;

    @BeforeAll
    static void setUp() {
        cone = new SecondOrderCone(2);
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
    void newSecondOrderConeWithZeroNThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> new SecondOrderCone(0));

        assertEquals("n must be positive", exception.getMessage());
    }
}
