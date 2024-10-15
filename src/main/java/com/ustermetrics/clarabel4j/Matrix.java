package com.ustermetrics.clarabel4j;

import lombok.Builder;
import lombok.NonNull;
import lombok.val;

import java.util.Arrays;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.toIntExact;

/**
 * A parameter object for a
 * <a href="https://en.wikipedia.org/wiki/Sparse_matrix#Compressed_sparse_column_(CSC_or_CCS)">sparse Column Compressed Storage (CCS) matrix</a>.
 *
 * @param m      number of rows
 * @param n      number of columns
 * @param colPtr column index
 * @param rowVal row index. Entries within each column need to appear in order of increasing row index.
 * @param nzVal  data
 */
@Builder
public record Matrix(int m, int n, long @NonNull [] colPtr, long @NonNull [] rowVal, double @NonNull [] nzVal) {

    public Matrix {
        checkArgument(m > 0, "number of rows must be positive");
        checkArgument(n > 0, "number of columns must be positive");
        checkArgument(colPtr.length > 0, "length of the column index must be positive");
        checkArgument(rowVal.length > 0, "length of the row index must be positive");
        checkArgument(nzVal.length > 0, "length of data must be positive");

        val nnz = nzVal.length;
        checkArgument(nnz == rowVal.length, "length of data must be equal to the length of the row index");
        checkArgument(colPtr.length == n + 1,
                "length of the column index must be equal to the number of columns plus one");
        checkArgument(nnz <= m * n,
                "number of non-zero entries must be less equal than the number of rows times the number of columns");
        checkArgument(Arrays.stream(rowVal).allMatch(i -> 0 <= i && i < m),
                "entries of the row index must be greater equal zero and less than the number of rows");
        checkArgument(colPtr[0] == 0 && colPtr[colPtr.length - 1] == nnz,
                "the first entry of the column index must be equal to zero and the last entry must be equal to the " +
                        "number of non-zero entries");
        checkArgument(IntStream.range(0, colPtr.length - 1)
                        .allMatch(i -> 0 <= colPtr[i] && colPtr[i] <= nnz && colPtr[i] <= colPtr[i + 1]
                                && IntStream.range(toIntExact(colPtr[i]), toIntExact(colPtr[i + 1]) - 1)
                                .allMatch(j -> rowVal[j] < rowVal[j + 1])),
                "entries of the column index must be greater equal zero, less equal than the number of non-zero " +
                        "entries, and must be ordered, entries of the row index within each column must be strictly " +
                        "ordered");
    }

}
