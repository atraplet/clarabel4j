package com.ustermetrics.clarabel4j;

import lombok.val;
import org.junit.jupiter.api.Test;

import static com.ustermetrics.clarabel4j.DirectSolveMethod.QDLDL;
import static org.junit.jupiter.api.Assertions.*;

class ParametersTest {

    @Test
    void buildParametersWithCustomOptionsReturnsParametersWithCustomOptions() {
        val parameters = Parameters.builder()
                .maxIter(1)
                .timeLimit(1.)
                .verbose(true)
                .maxStepFraction(1.)
                .tolGapAbs(1.)
                .tolGapRel(1.)
                .tolFeas(1.)
                .tolInfeasAbs(1.)
                .tolInfeasRel(1.)
                .tolKtratio(1.)
                .reducedTolGapAbs(1.)
                .reducedTolGapRel(1.)
                .reducedTolFeas(1.)
                .reducedTolInfeasAbs(1.)
                .reducedTolInfeasRel(1.)
                .reducedTolKtratio(1.)
                .equilibrateEnable(true)
                .equilibrateMaxIter(1)
                .equilibrateMinScaling(1.)
                .equilibrateMaxScaling(1.)
                .linesearchBacktrackStep(1.)
                .minSwitchStepLength(1.)
                .minTerminateStepLength(1.)
                .directKktSolver(true)
                .directSolveMethod(QDLDL)
                .staticRegularizationEnable(true)
                .staticRegularizationConstant(1.)
                .staticRegularizationProportional(1.)
                .dynamicRegularizationEnable(true)
                .dynamicRegularizationEps(1.)
                .dynamicRegularizationDelta(1.)
                .iterativeRefinementEnable(true)
                .iterativeRefinementReltol(1.)
                .iterativeRefinementAbstol(1.)
                .iterativeRefinementMaxIter(1)
                .iterativeRefinementStopRatio(1.)
                .presolveEnable(true)
                .build();

        val tol = 1e-8;
        assertEquals(1, parameters.maxIter());
        assertEquals(1., parameters.timeLimit(), tol);
        assertTrue(parameters.verbose());
        assertEquals(1., parameters.maxStepFraction(), tol);
        assertEquals(1., parameters.tolGapAbs(), tol);
        assertEquals(1., parameters.tolGapRel(), tol);
        assertEquals(1., parameters.tolFeas(), tol);
        assertEquals(1., parameters.tolInfeasAbs(), tol);
        assertEquals(1., parameters.tolInfeasRel(), tol);
        assertEquals(1., parameters.tolKtratio(), tol);
        assertEquals(1., parameters.reducedTolGapAbs(), tol);
        assertEquals(1., parameters.reducedTolGapRel(), tol);
        assertEquals(1., parameters.reducedTolFeas(), tol);
        assertEquals(1., parameters.reducedTolInfeasAbs(), tol);
        assertEquals(1., parameters.reducedTolInfeasRel(), tol);
        assertEquals(1., parameters.reducedTolKtratio(), tol);
        assertTrue(parameters.equilibrateEnable());
        assertEquals(1, parameters.equilibrateMaxIter());
        assertEquals(1., parameters.equilibrateMinScaling(), tol);
        assertEquals(1., parameters.equilibrateMaxScaling(), tol);
        assertEquals(1., parameters.linesearchBacktrackStep(), tol);
        assertEquals(1., parameters.minSwitchStepLength(), tol);
        assertEquals(1., parameters.minTerminateStepLength(), tol);
        assertTrue(parameters.directKktSolver());
        assertEquals(QDLDL, parameters.directSolveMethod());
        assertTrue(parameters.staticRegularizationEnable());
        assertEquals(1., parameters.staticRegularizationConstant(), tol);
        assertEquals(1., parameters.staticRegularizationProportional(), tol);
        assertTrue(parameters.dynamicRegularizationEnable());
        assertEquals(1., parameters.dynamicRegularizationEps(), tol);
        assertEquals(1., parameters.dynamicRegularizationDelta(), tol);
        assertTrue(parameters.iterativeRefinementEnable());
        assertEquals(1., parameters.iterativeRefinementReltol(), tol);
        assertEquals(1., parameters.iterativeRefinementAbstol(), tol);
        assertEquals(1, parameters.iterativeRefinementMaxIter());
        assertEquals(1., parameters.iterativeRefinementStopRatio(), tol);
        assertTrue(parameters.presolveEnable());
    }

    @Test
    void buildParametersWithInvalidMaxIterThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .maxIter(-1)
                .build());

        assertEquals("maxIter must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidTimeLimitThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .timeLimit(-1.)
                .build());

        assertEquals("timeLimit must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidMaxStepFractionThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .maxStepFraction(-1.)
                .build());

        assertEquals("maxStepFraction must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidTolGapAbsThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .tolGapAbs(-1.)
                .build());

        assertEquals("tolGapAbs must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidTolGapRelThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .tolGapRel(-1.)
                .build());

        assertEquals("tolGapRel must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidTolFeasThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .tolFeas(-1.)
                .build());

        assertEquals("tolFeas must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidTolInfeasAbsThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .tolInfeasAbs(-1.)
                .build());

        assertEquals("tolInfeasAbs must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidTolInfeasRelThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .tolInfeasRel(-1.)
                .build());

        assertEquals("tolInfeasRel must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidTolKtratioThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .tolKtratio(-1.)
                .build());

        assertEquals("tolKtratio must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidReducedTolGapAbsThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .reducedTolGapAbs(-1.)
                .build());

        assertEquals("reducedTolGapAbs must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidReducedTolGapRelThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .reducedTolGapRel(-1.)
                .build());

        assertEquals("reducedTolGapRel must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidReducedTolFeasThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .reducedTolFeas(-1.)
                .build());

        assertEquals("reducedTolFeas must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidReducedTolInfeasAbsThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .reducedTolInfeasAbs(-1.)
                .build());

        assertEquals("reducedTolInfeasAbs must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidReducedTolInfeasRelThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .reducedTolInfeasRel(-1.)
                .build());

        assertEquals("reducedTolInfeasRel must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidReducedTolKtratioThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .reducedTolKtratio(-1.)
                .build());

        assertEquals("reducedTolKtratio must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidEquilibrateMaxIterThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .equilibrateMaxIter(-1)
                .build());

        assertEquals("equilibrateMaxIter must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidEquilibrateMinScalingThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .equilibrateMinScaling(-1.)
                .build());

        assertEquals("equilibrateMinScaling must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidEquilibrateMaxScalingThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .equilibrateMaxScaling(-1.)
                .build());

        assertEquals("equilibrateMaxScaling must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidLinesearchBacktrackStepThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .linesearchBacktrackStep(-1.)
                .build());

        assertEquals("linesearchBacktrackStep must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidMinSwitchStepLengthThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .minSwitchStepLength(-1.)
                .build());

        assertEquals("minSwitchStepLength must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidMinTerminateStepLengthThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .minTerminateStepLength(-1.)
                .build());

        assertEquals("minTerminateStepLength must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidStaticRegularizationConstantThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .staticRegularizationConstant(-1.)
                .build());

        assertEquals("staticRegularizationConstant must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidStaticRegularizationProportionalThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .staticRegularizationProportional(-1.)
                .build());

        assertEquals("staticRegularizationProportional must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidDynamicRegularizationEpsThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .dynamicRegularizationEps(-1.)
                .build());

        assertEquals("dynamicRegularizationEps must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidDynamicRegularizationDeltaThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .dynamicRegularizationDelta(-1.)
                .build());

        assertEquals("dynamicRegularizationDelta must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidIterativeRefinementReltolThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .iterativeRefinementReltol(-1.)
                .build());

        assertEquals("iterativeRefinementReltol must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidIterativeRefinementAbstolThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .iterativeRefinementAbstol(-1.)
                .build());

        assertEquals("iterativeRefinementAbstol must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidIterativeRefinementMaxIterThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .iterativeRefinementMaxIter(-1)
                .build());

        assertEquals("iterativeRefinementMaxIter must be null or positive", exception.getMessage());
    }

    @Test
    void buildParametersWithInvalidIterativeRefinementStopRatioThrowsException() {
        val exception = assertThrowsExactly(IllegalArgumentException.class, () -> Parameters.builder()
                .iterativeRefinementStopRatio(-1.)
                .build());

        assertEquals("iterativeRefinementStopRatio must be null or positive", exception.getMessage());
    }

}
