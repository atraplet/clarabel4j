package com.ustermetrics.clarabel4j.bindings;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.lang.foreign.Arena;

import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.*;
import static java.lang.foreign.MemorySegment.NULL;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BindingsTest {

    @Test
    void solveLinearProgramReturnsExpectedSolution() {
        try (val arena = Arena.ofConfined()) {
            // Create linear program from the Clarabel examples
            // https://github.com/oxfordcontrol/Clarabel.cpp/blob/main/examples/c/example_lp.c.
            val pSeg = ClarabelCscMatrix_f64.allocate(arena);
            val pColPtrSeg = arena.allocateFrom(C_LONG_LONG, 0, 0, 0);
            clarabel_CscMatrix_f64_init(pSeg, 2, 2, pColPtrSeg, NULL, NULL);

            val qSeg = arena.allocateFrom(C_DOUBLE, 1., -1.);

            val aSeg = ClarabelCscMatrix_f64.allocate(arena);
            val aColPtrSeg = arena.allocateFrom(C_LONG_LONG, 0, 2, 4);
            val aRowValSeg = arena.allocateFrom(C_LONG_LONG, 0, 2, 1, 3);
            val aNzValSeg = arena.allocateFrom(C_DOUBLE, 1., -1., 1., -1.);
            clarabel_CscMatrix_f64_init(aSeg, 4, 2, aColPtrSeg, aRowValSeg, aNzValSeg);

            val bSeg = arena.allocateFrom(C_DOUBLE, 1., 1., 1., 1.);

            val conesSeg = ClarabelSupportedConeT_f64.allocateArray(1, arena);
            val coneSeg = ClarabelSupportedConeT_f64.asSlice(conesSeg, 0);
            ClarabelSupportedConeT_f64.nonnegative_cone_t(coneSeg, 4);
            ClarabelSupportedConeT_f64.tag(coneSeg, ClarabelNonnegativeConeT_Tag());

            // Create settings
            val settingsSeg = clarabel_DefaultSettings_f64_default(arena);
            ClarabelDefaultSettings_f64.equilibrate_enable(settingsSeg, true);
            ClarabelDefaultSettings_f64.max_iter(settingsSeg, 50);
            ClarabelDefaultSettings_f64.verbose(settingsSeg, false);

            // Create solver
            val solverSeg = clarabel_DefaultSolver_f64_new(pSeg, qSeg, aSeg, bSeg, 1, conesSeg, settingsSeg);

            // Solve
            clarabel_DefaultSolver_f64_solve(solverSeg);

            // Get solution
            val solutionSeg = ClarabelDefaultSolution_f64.reinterpret(clarabel_DefaultSolver_f64_solution(arena,
                    solverSeg), arena, null);

            // Get status
            val status = ClarabelDefaultSolution_f64.status(solutionSeg);
            assertEquals(ClarabelSolved(), status);

            // Get length of x
            val xLength = ClarabelDefaultSolution_f64.x_length(solutionSeg);
            assertEquals(2, xLength);

            // Get x
            val x = ClarabelDefaultSolution_f64.x(solutionSeg)
                    .reinterpret(C_DOUBLE.byteSize() * xLength, arena, null)
                    .toArray(C_DOUBLE);
            assertArrayEquals(new double[]{-1., 1.}, x, 1e-8);

            // Free solver
            clarabel_DefaultSolver_f64_free(solverSeg);
        }
    }

}
