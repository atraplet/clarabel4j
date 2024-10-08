package com.ustermetrics.clarabel4j;

import com.ustermetrics.clarabel4j.bindings.ClarabelCscMatrix_f64;
import com.ustermetrics.clarabel4j.bindings.ClarabelDefaultSettings_f64;
import com.ustermetrics.clarabel4j.bindings.ClarabelDefaultSolution_f64;
import com.ustermetrics.clarabel4j.bindings.ClarabelSupportedConeT_f64;
import lombok.NonNull;
import lombok.val;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.*;
import static java.lang.Math.toIntExact;
import static java.lang.foreign.MemorySegment.NULL;

/**
 * An optimization model which can be optimized with the <a href="https://clarabel.org">Clarabel</a> solver.
 * <p>
 * In order to control the lifecycle of native memory, {@link Model} implements the {@link AutoCloseable}
 * interface and should be used with the <i>try-with-resources</i> statement.
 */
public class Model implements AutoCloseable {

    private enum Stage {NEW, SETUP, OPTIMIZED}

    private final Arena arena = Arena.ofConfined();
    private Stage stage = Stage.NEW;
    private long nCones;
    private MemorySegment pSeg;
    private MemorySegment qSeg;
    private MemorySegment aSeg;
    private MemorySegment bSeg;
    private MemorySegment conesSeg;
    private MemorySegment settingsSeg;
    private MemorySegment solverSeg;
    private MemorySegment solutionSeg;

    /**
     * Set up the {@link Model} data for a convex optimization problem of type
     * <pre>
     * minimize        1/2 x'Px + q'x
     * subject to      Ax + s = b
     *                 s in K
     * </pre>
     * where x are the primal variables, s are slack variables, and P, q, A, and b are the model data. The convex set
     * K is a composition of convex cones. Supported cones are the Zero cone, the Nonnegative Orthant, the
     * Second-Order Cone, the Exponential Cone, the Power Cone, and the Generalized Power Cone.
     *
     * @param pNzVal  the (optional) cost function sparse P matrix data (Column Compressed Storage CCS).
     * @param pColPtr the (optional) cost function sparse P matrix column index (CCS).
     * @param pRowVal the (optional) cost function sparse P matrix row index (CCS). Entries within each column need
     *                to appear in order of increasing row index.
     * @param q       the (optional) cost function q weights.
     * @param aNzVal  the (optional) cone constraints sparse A matrix data (CCS).
     * @param aColPtr the (optional) cone constraints sparse A matrix column index (CCS).
     * @param aRowVal the (optional) cone constraints sparse A matrix row index (CCS). Entries within each column
     *                need to appear in order of increasing row index.
     * @param b       the right-hand-side of the cone constraints.
     * @param cones   the types and dimensions of the convex cones.
     * @see <a href="https://clarabel.org">Clarabel</a>
     */
    public void setup(double[] pNzVal, long[] pColPtr, long[] pRowVal, double[] q, double[] aNzVal, long[] aColPtr,
                      long[] aRowVal, double[] b, List<@NonNull Cone> cones) {
        checkArguments(pNzVal, pColPtr, pRowVal, q, aNzVal, aColPtr, aRowVal, b, cones);
        unsafeSetup(pNzVal, pColPtr, pRowVal, q, aNzVal, aColPtr, aRowVal, b, cones);
    }

    private static void checkArguments(double[] pNzVal, long[] pColPtr, long[] pRowVal, double[] q, double[] aNzVal,
                                       long[] aColPtr, long[] aRowVal, double[] b, List<Cone> cones) {
        checkArgument(pColPtr != null && pRowVal != null && pNzVal != null
                        || pColPtr == null && pRowVal == null && pNzVal == null,
                "all arguments of the matrix P must be supplied together or must be null together");
        checkArgument(aColPtr != null && aRowVal != null && aNzVal != null && b != null && cones != null
                        || aColPtr == null && aRowVal == null && aNzVal == null && b == null && cones == null,
                "all arguments of the matrix A, the array b, and the cones must be supplied together or must be null" +
                        " together");
        checkArgument(pColPtr != null || aColPtr != null, "matrix P or matrix A must be supplied");

        val nCols = nCols(pColPtr, aColPtr);
        if (pColPtr != null) {
            checkMatrix(pNzVal, pColPtr, pRowVal, nCols, nCols, "P");
        }

        if (q != null) {
            checkArgument(q.length == nCols,
                    "array q must have the same length as the number of columns (rows) of the matrix P (if supplied) " +
                            "or the number of columns of the matrix A (if supplied)");
        }

        if (aColPtr != null) {
            val nRows = b.length;
            checkMatrix(aNzVal, aColPtr, aRowVal, nRows, nCols, "A");
            checkArgument(nRows == cones.stream().mapToLong(Cone::getDimension).sum(),
                    "length of the array b must be equal to the dimension of the convex set K");
        }
    }

    private static int nCols(long[] pColPtr, long[] aColPtr) {
        if (pColPtr != null) {
            return pColPtr.length - 1;
        } else {
            return aColPtr.length - 1;
        }
    }

    private static void checkMatrix(double[] mNzVal, long[] mColPtr, long[] mRowVal, int nRows, int nCols,
                                    String mName) {
        checkArgument(nRows > 0, "matrix %s: number of rows must be positive", mName);
        checkArgument(nCols > 0, "matrix %s: number of columns must be positive", mName);

        val nnz = mNzVal.length;
        checkArgument(0 < nnz && nnz <= nRows * nCols,
                "matrix %s: number of non-zero entries must be greater than zero and less equal than the number of " +
                        "rows times the number of columns", mName);
        checkArgument(mRowVal.length == nnz,
                "matrix %s: number of entries in the row index must be equal to the number of non-zero entries", mName);
        checkArgument(mColPtr.length == nCols + 1,
                "length of the column index must be equal to the number of columns plus one", mName);
        checkArgument(Arrays.stream(mRowVal).allMatch(i -> 0 <= i && i < nRows),
                "matrix %s: entries of the row index must be greater equal zero and less than the number of rows",
                mName);
        checkArgument(mColPtr[0] == 0 && mColPtr[mColPtr.length - 1] == nnz,
                "matrix %s: the first entry of the column index must be equal to zero and the last entry must be " +
                        "equal to the number of non-zero entries", mName);
        checkArgument(IntStream.range(0, mColPtr.length - 1).allMatch(i ->
                        0 <= mColPtr[i] && mColPtr[i] <= nnz && mColPtr[i] <= mColPtr[i + 1]
                                && IntStream.range(toIntExact(mColPtr[i]), toIntExact(mColPtr[i + 1]) - 1).allMatch(j -> mRowVal[j] < mRowVal[j + 1])),
                "matrix %s: entries of the column index must be greater equal zero, less equal than the number of " +
                        "non-zero entries, and must be ordered, entries of the row index within each column must be " +
                        "strictly ordered", mName);
    }

    /**
     * Set up the {@link Model} data.
     * <p>
     * Same as
     * {@link Model#setup(double[] pNzVal, long[] pColPtr, long[] pRowVal, double[] q, double[] aNzVal, long[] aColPtr, long[] aRowVal, double[] b, List cones)}
     * without quadratic cost function part, i.e. {@code pNzVal}, {@code pColPtr}, and {@code pRowVal} are {@code null}.
     *
     * @param q       the (optional) cost function q weights.
     * @param aNzVal  the (optional) cone constraints sparse A matrix data (CCS).
     * @param aColPtr the (optional) cone constraints sparse A matrix column index (CCS).
     * @param aRowVal the (optional) cone constraints sparse A matrix row index (CCS). Entries within each column
     *                need to appear in order of increasing row index.
     * @param b       the right-hand-side of the cone constraints.
     * @param cones   the types and dimensions of the convex cones.
     */
    public void setup(double[] q, double[] aNzVal, long[] aColPtr, long[] aRowVal, double[] b,
                      List<@NonNull Cone> cones) {
        setup(null, null, null, q, aNzVal, aColPtr, aRowVal, b, cones);
    }

    /**
     * Set up the {@link Model} data.
     * <p>
     * Same as
     * {@link Model#setup(double[] pNzVal, long[] pColPtr, long[] pRowVal, double[] q, double[] aNzVal, long[] aColPtr, long[] aRowVal, double[] b, List cones)}
     * without linear cost function part, i.e. {@code q} is {@code null}.
     *
     * @param pNzVal  the (optional) cost function sparse P matrix data (Column Compressed Storage CCS).
     * @param pColPtr the (optional) cost function sparse P matrix column index (CCS).
     * @param pRowVal the (optional) cost function sparse P matrix row index (CCS). Entries within each column need
     *                to appear in order of increasing row index.
     * @param aNzVal  the (optional) cone constraints sparse A matrix data (CCS).
     * @param aColPtr the (optional) cone constraints sparse A matrix column index (CCS).
     * @param aRowVal the (optional) cone constraints sparse A matrix row index (CCS). Entries within each column
     *                need to appear in order of increasing row index.
     * @param b       the right-hand-side of the cone constraints.
     * @param cones   the types and dimensions of the convex cones.
     */
    public void setup(double[] pNzVal, long[] pColPtr, long[] pRowVal, double[] aNzVal, long[] aColPtr,
                      long[] aRowVal, double[] b, List<@NonNull Cone> cones) {
        setup(pNzVal, pColPtr, pRowVal, null, aNzVal, aColPtr, aRowVal, b, cones);
    }

    /**
     * Set up the {@link Model} data.
     * <p>
     * Same as
     * {@link Model#setup(double[] pNzVal, long[] pColPtr, long[] pRowVal, double[] q, double[] aNzVal, long[] aColPtr, long[] aRowVal, double[] b, List cones)}
     * without cost function, i.e. {@code pNzVal}, {@code pColPtr}, {@code pRowVal}, and {@code q} are
     * {@code null}.
     *
     * @param aNzVal  the (optional) cone constraints sparse A matrix data (CCS).
     * @param aColPtr the (optional) cone constraints sparse A matrix column index (CCS).
     * @param aRowVal the (optional) cone constraints sparse A matrix row index (CCS). Entries within each column
     *                need to appear in order of increasing row index.
     * @param b       the right-hand-side of the cone constraints.
     * @param cones   the types and dimensions of the convex cones.
     */
    public void setup(double[] aNzVal, long[] aColPtr, long[] aRowVal, double[] b, List<@NonNull Cone> cones) { //
        setup(null, null, null, null, aNzVal, aColPtr, aRowVal, b, cones);
    }

    /**
     * Set up the {@link Model} data.
     * <p>
     * Same as
     * {@link Model#setup(double[] pNzVal, long[] pColPtr, long[] pRowVal, double[] q, double[] aNzVal, long[] aColPtr, long[] aRowVal, double[] b, List cones)}
     * without cone constraints, i.e. {@code aNzVal}, {@code aColPtr}, {@code aRowVal}, {@code b}, and {@code cones}
     * are {@code null}.
     *
     * @param pNzVal  the (optional) cost function sparse P matrix data (Column Compressed Storage CCS).
     * @param pColPtr the (optional) cost function sparse P matrix column index (CCS).
     * @param pRowVal the (optional) cost function sparse P matrix row index (CCS). Entries within each column need
     *                to appear in order of increasing row index.
     * @param q       the (optional) cost function q weights.
     */
    public void setup(double[] pNzVal, long[] pColPtr, long[] pRowVal, double[] q) {
        setup(pNzVal, pColPtr, pRowVal, q, null, null, null, null, null);
    }

    /**
     * Set up the {@link Model} data.
     * <p>
     * Same as
     * {@link Model#setup(double[] pNzVal, long[] pColPtr, long[] pRowVal, double[] q, double[] aNzVal, long[] aColPtr, long[] aRowVal, double[] b, List cones)}
     * without linear cost function part and cone constraints, i.e. {@code q}, {@code aNzVal}, {@code aColPtr},
     * {@code aRowVal}, {@code b}, and {@code cones} are {@code null}.
     *
     * @param pNzVal  the (optional) cost function sparse P matrix data (Column Compressed Storage CCS).
     * @param pColPtr the (optional) cost function sparse P matrix column index (CCS).
     * @param pRowVal the (optional) cost function sparse P matrix row index (CCS). Entries within each column need
     *                to appear in order of increasing row index.
     */
    public void setup(double[] pNzVal, long[] pColPtr, long[] pRowVal) {
        setup(pNzVal, pColPtr, pRowVal, null, null, null, null, null, null);
    }

    /**
     * Unsafe set up the {@link Model} data.
     * <p>
     * Same as
     * {@link Model#setup(double[] pNzVal, long[] pColPtr, long[] pRowVal, double[] q, double[] aNzVal, long[] aColPtr, long[] aRowVal, double[] b, List cones)}
     * without any precondition checks on its arguments.
     * <p>
     * <b>Warning: Setting the arguments incorrectly may lead to incorrect results in the best case. In the worst
     * case, it can crash the JVM and may silently result in memory corruption.</b>
     *
     * @param pNzVal  the (optional) cost function sparse P matrix data (Column Compressed Storage CCS).
     * @param pColPtr the (optional) cost function sparse P matrix column index (CCS).
     * @param pRowVal the (optional) cost function sparse P matrix row index (CCS). Entries within each column need
     *                to appear in order of increasing row index.
     * @param q       the (optional) cost function q weights.
     * @param aNzVal  the (optional) cone constraints sparse A matrix data (CCS).
     * @param aColPtr the (optional) cone constraints sparse A matrix column index (CCS).
     * @param aRowVal the (optional) cone constraints sparse A matrix row index (CCS). Entries within each column
     *                need to appear in order of increasing row index.
     * @param b       the right-hand-side of the cone constraints.
     * @param cones   the types and dimensions of the convex cones.
     */
    public void unsafeSetup(double[] pNzVal, long[] pColPtr, long[] pRowVal, double[] q, double[] aNzVal,
                            long[] aColPtr, long[] aRowVal, double[] b, List<@NonNull Cone> cones) {
        checkState(stage == Stage.NEW, "model must be in stage new");

        val nCols = nCols(pColPtr, aColPtr);
        pSeg = createMatrixSegment(pNzVal, pColPtr, pRowVal, nCols, nCols);
        qSeg = q != null ? arena.allocateFrom(C_DOUBLE, q) : arena.allocateFrom(C_DOUBLE);

        val nRows = b != null ? b.length : 0;
        aSeg = createMatrixSegment(aNzVal, aColPtr, aRowVal, nRows, nCols);
        bSeg = b != null ? arena.allocateFrom(C_DOUBLE, b) : arena.allocateFrom(C_DOUBLE);

        nCones = cones != null ? cones.size() : 0;
        conesSeg = createConesSegment(cones);

        settingsSeg = clarabel_DefaultSettings_f64_default(arena);

        stage = Stage.SETUP;
    }

    private MemorySegment createMatrixSegment(double[] mNzVal, long[] mColPtr, long[] mRowVal, int nRows, int nCols) {
        val mSeg = ClarabelCscMatrix_f64.allocate(arena);

        final MemorySegment mNzValSeg, mColPtrSeg, mRowValSeg;
        if (mColPtr != null) {
            mNzValSeg = arena.allocateFrom(C_DOUBLE, mNzVal);
            mColPtrSeg = arena.allocateFrom(C_LONG_LONG, mColPtr);
            mRowValSeg = arena.allocateFrom(C_LONG_LONG, mRowVal);
        } else {
            mNzValSeg = NULL;
            mColPtrSeg = arena.allocateFrom(C_LONG_LONG, new long[nCols + 1]);
            mRowValSeg = NULL;
        }

        clarabel_CscMatrix_f64_init(mSeg, nRows, nCols, mColPtrSeg, mRowValSeg, mNzValSeg);

        return mSeg;
    }

    private MemorySegment createConesSegment(List<Cone> cones) {
        val nCones = cones != null ? cones.size() : 0;
        val conesSeg = ClarabelSupportedConeT_f64.allocateArray(nCones, arena);

        for (int i = 0; i < nCones; i++) {
            val coneSeg = ClarabelSupportedConeT_f64.asSlice(conesSeg, i);
            val cone = cones.get(i);

            switch (cone) {
                case ZeroCone zeroCone -> ClarabelSupportedConeT_f64.zero_cone_t(coneSeg, zeroCone.getN());
                case NonnegativeCone nonnegativeCone ->
                        ClarabelSupportedConeT_f64.nonnegative_cone_t(coneSeg, nonnegativeCone.getN());
                case SecondOrderCone secondOrderCone ->
                        ClarabelSupportedConeT_f64.second_order_cone_t(coneSeg, secondOrderCone.getN());
                case ExponentialCone _ -> {
                }
                case PowerCone powerCone -> ClarabelSupportedConeT_f64.power_cone_t(coneSeg, powerCone.getA());
                case GenPowerCone genPowerCone -> {
                    val alphaSeg = arena.allocateFrom(C_DOUBLE, genPowerCone.getA());
                    ClarabelSupportedConeT_f64.genpow_cone_alpha_t(coneSeg, alphaSeg);
                    ClarabelSupportedConeT_f64.genpow_cone_dim1_t(coneSeg, genPowerCone.getA().length);
                    ClarabelSupportedConeT_f64.genpow_cone_dim2_t(coneSeg, genPowerCone.getN());
                }
            }

            ClarabelSupportedConeT_f64.tag(coneSeg, cone.getTag());
        }

        return conesSeg;
    }

    /**
     * Sets the <a href="https://clarabel.org">Clarabel</a> solver settings.
     * <p>
     * For {@code null} settings solver defaults are applied.
     *
     * @param parameters the parameter object for the solver settings.
     */
    public void setParameters(@NonNull Parameters parameters) {
        checkState(stage != Stage.NEW, "Model must not be in stage new");

        Optional.ofNullable(parameters.maxIter())
                .ifPresent(maxIter -> ClarabelDefaultSettings_f64.max_iter(settingsSeg, maxIter));
        Optional.ofNullable(parameters.timeLimit())
                .ifPresent(timeLimit -> ClarabelDefaultSettings_f64.time_limit(settingsSeg, timeLimit));
        Optional.ofNullable(parameters.verbose())
                .ifPresent(verbose -> ClarabelDefaultSettings_f64.verbose(settingsSeg, verbose));
        Optional.ofNullable(parameters.maxStepFraction())
                .ifPresent(maxStepFraction ->
                        ClarabelDefaultSettings_f64.max_step_fraction(settingsSeg, maxStepFraction));
        Optional.ofNullable(parameters.tolGapAbs())
                .ifPresent(tolGapAbs -> ClarabelDefaultSettings_f64.tol_gap_abs(settingsSeg, tolGapAbs));
        Optional.ofNullable(parameters.tolGapRel())
                .ifPresent(tolGapRel -> ClarabelDefaultSettings_f64.tol_gap_rel(settingsSeg, tolGapRel));
        Optional.ofNullable(parameters.tolFeas())
                .ifPresent(tolFeas -> ClarabelDefaultSettings_f64.tol_feas(settingsSeg, tolFeas));
        Optional.ofNullable(parameters.tolInfeasAbs())
                .ifPresent(tolInfeasAbs -> ClarabelDefaultSettings_f64.tol_infeas_abs(settingsSeg, tolInfeasAbs));
        Optional.ofNullable(parameters.tolInfeasRel())
                .ifPresent(tolInfeasRel -> ClarabelDefaultSettings_f64.tol_infeas_rel(settingsSeg, tolInfeasRel));
        Optional.ofNullable(parameters.tolKtratio())
                .ifPresent(tolKtratio -> ClarabelDefaultSettings_f64.tol_ktratio(settingsSeg, tolKtratio));
        Optional.ofNullable(parameters.reducedTolGapAbs())
                .ifPresent(reducedTolGapAbs ->
                        ClarabelDefaultSettings_f64.reduced_tol_gap_abs(settingsSeg, reducedTolGapAbs));
        Optional.ofNullable(parameters.reducedTolGapRel())
                .ifPresent(reducedTolGapRel ->
                        ClarabelDefaultSettings_f64.reduced_tol_gap_rel(settingsSeg, reducedTolGapRel));
        Optional.ofNullable(parameters.reducedTolFeas())
                .ifPresent(reducedTolFeas -> ClarabelDefaultSettings_f64.reduced_tol_feas(settingsSeg, reducedTolFeas));
        Optional.ofNullable(parameters.reducedTolInfeasAbs())
                .ifPresent(reducedTolInfeasAbs ->
                        ClarabelDefaultSettings_f64.reduced_tol_infeas_abs(settingsSeg, reducedTolInfeasAbs));
        Optional.ofNullable(parameters.reducedTolInfeasRel())
                .ifPresent(reducedTolInfeasRel ->
                        ClarabelDefaultSettings_f64.reduced_tol_infeas_rel(settingsSeg, reducedTolInfeasRel));
        Optional.ofNullable(parameters.reducedTolKtratio())
                .ifPresent(reducedTolKtratio ->
                        ClarabelDefaultSettings_f64.reduced_tol_ktratio(settingsSeg, reducedTolKtratio));
        Optional.ofNullable(parameters.equilibrateEnable())
                .ifPresent(equilibrateEnable ->
                        ClarabelDefaultSettings_f64.equilibrate_enable(settingsSeg, equilibrateEnable));
        Optional.ofNullable(parameters.equilibrateMaxIter())
                .ifPresent(equilibrateMaxIter ->
                        ClarabelDefaultSettings_f64.equilibrate_max_iter(settingsSeg, equilibrateMaxIter));
        Optional.ofNullable(parameters.equilibrateMinScaling())
                .ifPresent(equilibrateMinScaling ->
                        ClarabelDefaultSettings_f64.equilibrate_min_scaling(settingsSeg, equilibrateMinScaling));
        Optional.ofNullable(parameters.equilibrateMaxScaling())
                .ifPresent(equilibrateMaxScaling ->
                        ClarabelDefaultSettings_f64.equilibrate_max_scaling(settingsSeg, equilibrateMaxScaling));
        Optional.ofNullable(parameters.linesearchBacktrackStep())
                .ifPresent(linesearchBacktrackStep ->
                        ClarabelDefaultSettings_f64.linesearch_backtrack_step(settingsSeg, linesearchBacktrackStep));
        Optional.ofNullable(parameters.minSwitchStepLength())
                .ifPresent(minSwitchStepLength ->
                        ClarabelDefaultSettings_f64.min_switch_step_length(settingsSeg, minSwitchStepLength));
        Optional.ofNullable(parameters.minTerminateStepLength())
                .ifPresent(minTerminateStepLength ->
                        ClarabelDefaultSettings_f64.min_terminate_step_length(settingsSeg, minTerminateStepLength));
        Optional.ofNullable(parameters.directKktSolver())
                .ifPresent(directKktSolver ->
                        ClarabelDefaultSettings_f64.direct_kkt_solver(settingsSeg, directKktSolver));
        Optional.ofNullable(parameters.directSolveMethod())
                .ifPresent(directSolveMethod ->
                        ClarabelDefaultSettings_f64.direct_solve_method(settingsSeg, directSolveMethod.method()));
        Optional.ofNullable(parameters.staticRegularizationEnable())
                .ifPresent(staticRegularizationEnable ->
                        ClarabelDefaultSettings_f64.static_regularization_enable(settingsSeg,
                                staticRegularizationEnable));
        Optional.ofNullable(parameters.staticRegularizationConstant())
                .ifPresent(staticRegularizationConstant ->
                        ClarabelDefaultSettings_f64.static_regularization_constant(settingsSeg,
                                staticRegularizationConstant));
        Optional.ofNullable(parameters.staticRegularizationProportional())
                .ifPresent(staticRegularizationProportional ->
                        ClarabelDefaultSettings_f64.static_regularization_proportional(settingsSeg,
                                staticRegularizationProportional));
        Optional.ofNullable(parameters.dynamicRegularizationEnable())
                .ifPresent(dynamicRegularizationEnable ->
                        ClarabelDefaultSettings_f64.dynamic_regularization_enable(settingsSeg,
                                dynamicRegularizationEnable));
        Optional.ofNullable(parameters.dynamicRegularizationEps())
                .ifPresent(dynamicRegularizationEps ->
                        ClarabelDefaultSettings_f64.dynamic_regularization_eps(settingsSeg, dynamicRegularizationEps));
        Optional.ofNullable(parameters.dynamicRegularizationDelta())
                .ifPresent(dynamicRegularizationDelta ->
                        ClarabelDefaultSettings_f64.dynamic_regularization_delta(settingsSeg,
                                dynamicRegularizationDelta));
        Optional.ofNullable(parameters.iterativeRefinementEnable())
                .ifPresent(iterativeRefinementEnable ->
                        ClarabelDefaultSettings_f64.iterative_refinement_enable(settingsSeg,
                                iterativeRefinementEnable));
        Optional.ofNullable(parameters.iterativeRefinementReltol())
                .ifPresent(iterativeRefinementReltol ->
                        ClarabelDefaultSettings_f64.iterative_refinement_reltol(settingsSeg,
                                iterativeRefinementReltol));
        Optional.ofNullable(parameters.iterativeRefinementAbstol())
                .ifPresent(iterativeRefinementAbstol ->
                        ClarabelDefaultSettings_f64.iterative_refinement_abstol(settingsSeg,
                                iterativeRefinementAbstol));
        Optional.ofNullable(parameters.iterativeRefinementMaxIter())
                .ifPresent(iterativeRefinementMaxIter ->
                        ClarabelDefaultSettings_f64.iterative_refinement_max_iter(settingsSeg,
                                iterativeRefinementMaxIter));
        Optional.ofNullable(parameters.iterativeRefinementStopRatio())
                .ifPresent(iterativeRefinementStopRatio ->
                        ClarabelDefaultSettings_f64.iterative_refinement_stop_ratio(settingsSeg,
                                iterativeRefinementStopRatio));
        Optional.ofNullable(parameters.presolveEnable())
                .ifPresent(presolveEnable -> ClarabelDefaultSettings_f64.presolve_enable(settingsSeg, presolveEnable));
    }

    /**
     * Optimizes this {@link Model} with the<a href="https://clarabel.org">Clarabel</a> solver.
     *
     * @return the solver status.
     */
    public Status optimize() {
        checkState(stage != Stage.NEW, "model must not be in stage new");

        solverSeg = clarabel_DefaultSolver_f64_new(pSeg, qSeg, aSeg, bSeg, nCones, conesSeg, settingsSeg);
        clarabel_DefaultSolver_f64_solve(solverSeg);
        solutionSeg = ClarabelDefaultSolution_f64.reinterpret(clarabel_DefaultSolver_f64_solution(arena, solverSeg),
                arena, null);

        val status = Status.valueOf(ClarabelDefaultSolution_f64.status(solutionSeg));
        stage = Stage.OPTIMIZED;

        return status;
    }

    /**
     * Cleanup: free this {@link Model} native memory.
     */
    public void cleanup() {
        checkState(stage != Stage.NEW, "model must not be in stage new");
        clarabel_DefaultSolver_f64_free(solverSeg);
        stage = Stage.NEW;
    }

    /**
     * @return the primal variables of this optimized {@link Model}.
     * @see <a href="https://clarabel.org">Clarabel</a>
     */
    public double @NonNull [] x() {
        checkStageIsOptimized();
        val xLength = ClarabelDefaultSolution_f64.x_length(solutionSeg);
        return ClarabelDefaultSolution_f64.x(solutionSeg)
                .reinterpret(C_DOUBLE.byteSize() * xLength, arena, null)
                .toArray(C_DOUBLE);
    }

    /**
     * @return the dual variables of this optimized {@link Model}.
     * @see <a href="https://clarabel.org">Clarabel</a>
     */
    public double @NonNull [] z() {
        checkStageIsOptimized();
        val zLength = ClarabelDefaultSolution_f64.z_length(solutionSeg);
        return ClarabelDefaultSolution_f64.z(solutionSeg)
                .reinterpret(C_DOUBLE.byteSize() * zLength, arena, null)
                .toArray(C_DOUBLE);
    }

    /**
     * @return the slack variables of this optimized {@link Model}.
     * @see <a href="https://clarabel.org">Clarabel</a>
     */
    public double @NonNull [] s() {
        checkStageIsOptimized();
        val sLength = ClarabelDefaultSolution_f64.s_length(solutionSeg);
        return ClarabelDefaultSolution_f64.s(solutionSeg)
                .reinterpret(C_DOUBLE.byteSize() * sLength, arena, null)
                .toArray(C_DOUBLE);
    }

    /**
     * @return the primal objective of this optimized {@link Model}.
     * @see <a href="https://clarabel.org">Clarabel</a>
     */
    public double objVal() {
        checkStageIsOptimized();
        return ClarabelDefaultSolution_f64.obj_val(solutionSeg);
    }

    /**
     * @return the dual objective of this optimized {@link Model}.
     * @see <a href="https://clarabel.org">Clarabel</a>
     */
    public double objValDual() {
        checkStageIsOptimized();
        return ClarabelDefaultSolution_f64.obj_val_dual(solutionSeg);
    }

    /**
     * @return the time needed until this {@link Model} was optimized.
     * @see <a href="https://clarabel.org">Clarabel</a>
     */
    public double solveTime() {
        checkStageIsOptimized();
        return ClarabelDefaultSolution_f64.solve_time(solutionSeg);
    }

    /**
     * @return the performed number of iterations until this {@link Model} was optimized.
     * @see <a href="https://clarabel.org">Clarabel</a>
     */
    public int iterations() {
        checkStageIsOptimized();
        return ClarabelDefaultSolution_f64.iterations(solutionSeg);
    }

    /**
     * @return the primal residual of this optimized {@link Model}.
     * @see <a href="https://clarabel.org">Clarabel</a>
     */
    public double rPrim() {
        checkStageIsOptimized();
        return ClarabelDefaultSolution_f64.r_prim(solutionSeg);
    }

    /**
     * @return the dual residual of this optimized {@link Model}.
     * @see <a href="https://clarabel.org">Clarabel</a>
     */
    public double rDual() {
        checkStageIsOptimized();
        return ClarabelDefaultSolution_f64.r_dual(solutionSeg);
    }

    private void checkStageIsOptimized() {
        checkState(stage == Stage.OPTIMIZED, "model must be in stage optimized");
    }

    @Override
    public void close() {
        if (stage != Stage.NEW) {
            clarabel_DefaultSolver_f64_free(solverSeg);
        }
        arena.close();
    }

}
