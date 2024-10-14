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
 * @param nzVal  data
 * @param rowVal row index. Entries within each column need to appear in order of increasing row index.
 * @param colPtr column index
 * @param nRows  number of rows
 * @param nCols  number of columns
 */
@Builder
public record Matrix(double @NonNull [] nzVal, long @NonNull [] rowVal, long @NonNull [] colPtr, int nRows, int nCols) {

    public Matrix {
        checkArgument(nRows > 0, "number of rows must be positive");
        checkArgument(nCols > 0, "number of columns must be positive");

        val nnz = nzVal.length;
        checkArgument(nnz == rowVal.length,
                "length of data must be equal to the length of the row index");
        checkArgument(colPtr.length == nCols + 1,
                "length of the column index must be equal to the number of columns plus one");
        checkArgument(0 < nnz && nnz <= nRows * nCols,
                "number of non-zero entries must be greater than zero and less equal than the number of rows times " +
                        "the number of columns");
        checkArgument(Arrays.stream(rowVal).allMatch(i -> 0 <= i && i < nRows),
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
