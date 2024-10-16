package com.ustermetrics.clarabel4j;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {

    @Test
    void createMatrixReturnsMatrix() {
        val matrix = new Matrix(2, 2, new long[]{0, 1, 2}, new long[]{0, 1}, new double[]{6., 4.});

        assertEquals(2, matrix.m());
        assertEquals(2, matrix.n());
        assertArrayEquals(new long[]{0, 1, 2}, matrix.colPtr());
        assertArrayEquals(new long[]{0, 1}, matrix.rowVal());
        assertArrayEquals(new double[]{6., 4.}, matrix.nzVal(), 1e-8);
    }

    @Test
    void createZeroMatrixReturnsZeroMatrix() {
        val matrix = new Matrix(2, 2, new long[]{0, 0, 0}, new long[]{}, new double[]{});

        assertEquals(2, matrix.m());
        assertEquals(2, matrix.n());
        assertArrayEquals(new long[]{0, 0, 0}, matrix.colPtr());
        assertEquals(0, matrix.rowVal().length);
        assertEquals(0, matrix.nzVal().length);
    }

    @Test
    void createMatrixWithZeroNumberOfRowsThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () ->
                new Matrix(0, 0, new long[]{}, new long[]{}, new double[]{})
        );

        assertEquals("number of rows must be positive", exception.getMessage());
    }

    @Test
    void createMatrixWithZeroNumberOfColumnsThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () ->
                new Matrix(1, 0, new long[]{}, new long[]{}, new double[]{})
        );

        assertEquals("number of columns must be positive", exception.getMessage());
    }

    @Test
    void createMatrixWithInvalidDataThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () ->
                new Matrix(2, 2, new long[]{0, 1, 2}, new long[]{0, 1}, new double[]{6., 4., 5.})
        );

        assertEquals("length of data must be equal to the length of the row index", exception.getMessage());
    }

    @Test
    void createMatrixWithInvalidColumnIndexThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () ->
                new Matrix(2, 2, new long[]{0, 1, 2, 3}, new long[]{0, 1}, new double[]{6., 4.})
        );

        assertEquals("length of the column index must be equal to the number of columns plus one",
                exception.getMessage());
    }

    @Test
    void createMatrixWithInvalidNumberOfNonZeroEntriesThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () ->
                new Matrix(2, 2, new long[]{0, 1, 2}, new long[]{0, 1, 2, 3, 4}, new double[]{0., 1., 2., 3., 4.})
        );

        assertEquals("number of non-zero entries must be less equal than the number of rows times the number of " +
                "columns", exception.getMessage());
    }

    @Test
    void createMatrixWithInvalidRowIndexThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () ->
                new Matrix(2, 2, new long[]{0, 1, 2}, new long[]{-1, 1}, new double[]{6., 4.})
        );

        assertEquals("entries of the row index must be greater equal zero and less than the number of rows",
                exception.getMessage());
    }

    @Test
    void createMatrixWithInvalidColumnIndexEntriesThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () ->
                new Matrix(2, 2, new long[]{-1, 1, 2}, new long[]{0, 1}, new double[]{6., 4.})
        );

        assertEquals("the first entry of the column index must be equal to zero and the last entry must be equal to " +
                "the number of non-zero entries", exception.getMessage());
    }

    @Test
    void createMatrixWithTooLargeEntryInColumnIndexThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () ->
                new Matrix(2, 2, new long[]{0, 3, 2}, new long[]{0, 1}, new double[]{6., 4.})
        );

        assertEquals("entries of the column index must be greater equal zero, less equal than the number of non-zero " +
                "entries, and must be ordered", exception.getMessage());
    }

    @Test
    void createMatrixWithUnorderedRowIndexWithinColumnThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () ->
                new Matrix(5, 4, new long[]{0, 2, 5, 7, 8}, new long[]{0, 3, 1, 4, 3, 0, 4, 4},
                        new double[]{1., 4., 3., 5., 6., 2., 7., 8.})
        );

        assertEquals("entries of the row index within each column must be strictly ordered", exception.getMessage());
    }

}
