package com.ustermetrics.clarabel4j;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ustermetrics.clarabel4j.Status.SOLVED;
import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    @Test
    void solveLinearProgramReturnsExpectedSolution() {
        // Linear program from the Clarabel examples
        // https://github.com/oxfordcontrol/Clarabel.cpp/blob/main/examples/c/example_lp.c.
        try (val model = new Model()) {
            model.setup(new double[]{1., -1.}, new double[]{1., -1., 1., -1.}, new long[]{0, 2, 4},
                    new long[]{0, 2, 1, 3}, new double[]{1., 1., 1., 1.}, List.of(new NonnegativeCone(4)));

            val parameters = Parameters.builder()
                    .maxIter(50)
                    .verbose(false)
                    .build();
            model.setParameters(parameters);

            val status = model.optimize();

            assertEquals(SOLVED, status);
            val tol = 1e-8;
            assertArrayEquals(new double[]{-1., 1.}, model.x(), tol);
            assertArrayEquals(new double[]{0., 1., 1., 0.}, model.z(), tol);
            assertArrayEquals(new double[]{2., 0., 0., 2.}, model.s(), tol);
            assertEquals(-2., model.objVal(), tol);
            assertEquals(-2., model.objValDual(), tol);
            assertTrue(model.solveTime() > 0.);
            assertEquals(6, model.iterations());
            assertEquals(0., model.rPrim(), tol);
            assertEquals(0., model.rDual(), tol);
        }
    }

}
