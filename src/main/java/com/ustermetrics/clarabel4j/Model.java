package com.ustermetrics.clarabel4j;

import com.ustermetrics.clarabel4j.bindings.ClarabelCscMatrix_f64;
import com.ustermetrics.clarabel4j.bindings.ClarabelDefaultSettings_f64;
import com.ustermetrics.clarabel4j.bindings.ClarabelDefaultSolution_f64;
import com.ustermetrics.clarabel4j.bindings.ClarabelSupportedConeT_f64;
import lombok.NonNull;
import lombok.val;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.*;
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
     * Set up this {@link Model} data for a convex optimization problem of type
     * <pre>
     * minimize        1/2 x'Px + q'x
     * subject to      Ax + s = b
     *                 s in K
     * </pre>
     * where x are the primal variables, s are slack variables, and P, q, A, and b are the model data. The convex set
     * K is a composition of convex cones. Supported cones are the Zero cone, the Nonnegative Orthant, the
     * Second-Order Cone, the Exponential Cone, the Power Cone, and the Generalized Power Cone.
     *
     * @param p     (optional) cost function matrix P. P is assumed to be positive semi-definite and only values in
     *              the upper triangular part of P need to be supplied.
     * @param q     (optional) cost function weights q
     * @param a     (optional) cone constraints matrix A
     * @param b     (optional) right-hand-side of the cone constraints
     * @param cones (optional) types and dimensions of the convex cones
     * @see <a href="https://clarabel.org">Clarabel</a>
     */
    public void setup(Matrix p, double[] q, Matrix a, double[] b, List<@NonNull Cone> cones) {
        checkArguments(p, q, a, b, cones);
        unsafeSetup(p, q, a, b, cones);
    }

    private static void checkArguments(Matrix p, double[] q, Matrix a, double[] b, List<Cone> cones) {
        checkArgument(p != null || a != null, "P or A must be supplied");
        checkArgument(a != null && b != null && cones != null || a == null && b == null && cones == null,
                "A, b, and cones must be supplied together or must be null together");

        checkArgument(p == null || p.m() == p.n(), "P must be null or a square matrix");
        checkArgument(q == null || q.length > 0, "q must be null or the length must be positive");
        checkArgument(b == null || b.length > 0, "b must be null or the length must be positive");
        checkArgument(cones == null || !cones.isEmpty(), "cones must be null or not empty");

        checkArgument(p == null || q == null || p.n() == q.length,
                "P or q must be null or the number of columns of P must be equal to the length of q");
        checkArgument(p == null || a == null || p.n() == a.n(),
                "P or A must be null or the number of columns of P must be equal to the number of columns of A");
        checkArgument(q == null || a == null || q.length == a.n(),
                "q or A must be null or the length of q must be equal to the number of columns of A");
        checkArgument(a == null || a.m() == b.length,
                "A must be null or the number of rows of A must be equal to the length of b");
        checkArgument(a == null || a.m() == cones.stream().mapToLong(Cone::getDimension).sum(),
                "A must be null or the number of rows of A must be equal to the dimension of the convex set K");
    }

    /**
     * Set up the {@link Model} data.
     * <p>
     * Same as {@link Model#setup(Matrix p, double[] q, Matrix a, double[] b, List cones)} without quadratic cost
     * function part, i.e. {@code p} is {@code null}.
     *
     * @param q     (optional) cost function weights q
     * @param a     cone constraints matrix A
     * @param b     right-hand-side of the cone constraints
     * @param cones types and dimensions of the convex cones
     */
    public void setup(double[] q, @NonNull Matrix a, double @NonNull [] b, @NonNull List<@NonNull Cone> cones) {
        setup(null, q, a, b, cones);
    }

    /**
     * Set up the {@link Model} data.
     * <p>
     * Same as {@link Model#setup(Matrix p, double[] q, Matrix a, double[] b, List cones)} without linear cost
     * function part, i.e. {@code q} is {@code null}.
     *
     * @param p     (optional) cost function matrix P. P is assumed to be positive semi-definite and only values in
     *              the upper triangular part of P need to be supplied.
     * @param a     (optional) cone constraints matrix A
     * @param b     (optional) right-hand-side of the cone constraints
     * @param cones (optional) types and dimensions of the convex cones
     */
    public void setup(Matrix p, Matrix a, double[] b, List<@NonNull Cone> cones) {
        setup(p, null, a, b, cones);
    }

    /**
     * Set up the {@link Model} data.
     * <p>
     * Same as {@link Model#setup(Matrix p, double[] q, Matrix a, double[] b, List cones)} without cost function, i.e.
     * {@code p} and {@code q} are {@code null}.
     *
     * @param a     cone constraints matrix A
     * @param b     right-hand-side of the cone constraints
     * @param cones types and dimensions of the convex cones
     */
    public void setup(@NonNull Matrix a, double @NonNull [] b, @NonNull List<@NonNull Cone> cones) { //
        setup(null, null, a, b, cones);
    }

    /**
     * Set up the {@link Model} data.
     * <p>
     * Same as {@link Model#setup(Matrix p, double[] q, Matrix a, double[] b, List cones)} without cone constraints,
     * i.e. {@code a}, {@code b}, and {@code cones} are {@code null}.
     *
     * @param p cost function matrix P. P is assumed to be positive semi-definite and only values in the upper
     *          triangular part of P need to be supplied.
     * @param q (optional) cost function weights q
     */
    public void setup(@NonNull Matrix p, double[] q) {
        setup(p, q, null, null, null);
    }

    /**
     * Set up the {@link Model} data.
     * <p>
     * Same as {@link Model#setup(Matrix p, double[] q, Matrix a, double[] b, List cones)} without linear cost
     * function part and cone constraints, i.e. {@code q}, {@code a}, {@code b}, and {@code cones} are {@code null}.
     *
     * @param p cost function matrix P. P is assumed to be positive semi-definite and only values in the upper
     *          triangular part of P need to be supplied.
     */
    public void setup(@NonNull Matrix p) {
        setup(p, null, null, null, null);
    }

    /**
     * Unsafe set up the {@link Model} data.
     * <p>
     * Same as {@link Model#setup(Matrix p, double[] q, Matrix a, double[] b, List cones)} without any precondition
     * checks on its arguments.
     * <p>
     * <b>Warning: Setting the arguments incorrectly may lead to incorrect results in the best case. In the worst
     * case, it can crash the JVM and may silently result in memory corruption.</b>
     *
     * @param p     (optional) cost function matrix P. P is assumed to be positive semi-definite and only values in
     *              the upper triangular part of P need to be supplied.
     * @param q     (optional) cost function weights q
     * @param a     (optional) cone constraints matrix A
     * @param b     (optional) right-hand-side of the cone constraints
     * @param cones (optional) types and dimensions of the convex cones
     */
    public void unsafeSetup(Matrix p, double[] q, Matrix a, double[] b, List<@NonNull Cone> cones) {
        checkState(stage == Stage.NEW, "model must be in stage new");

        pSeg = p != null ? createMatrixSegment(p) : createNullMatrixSegment(a.n(), a.n());
        qSeg = q != null ? createArraySegment(q) : createNullArraySegment();

        aSeg = a != null ? createMatrixSegment(a) : createNullMatrixSegment(0, p.n());
        bSeg = b != null ? createArraySegment(b) : createNullArraySegment();

        nCones = cones != null ? cones.size() : 0;
        conesSeg = cones != null ? createConesSegment(cones) : createNullConesSegment();

        settingsSeg = clarabel_DefaultSettings_f64_default(arena);

        stage = Stage.SETUP;
    }

    private MemorySegment createMatrixSegment(Matrix matrix) {
        val matrixSeg = ClarabelCscMatrix_f64.allocate(arena);
        val colPtrSeg = arena.allocateFrom(C_LONG_LONG, matrix.colPtr());
        val rowValSeg = arena.allocateFrom(C_LONG_LONG, matrix.rowVal());
        val nzValSeg = arena.allocateFrom(C_DOUBLE, matrix.nzVal());
        clarabel_CscMatrix_f64_init(matrixSeg, matrix.m(), matrix.n(), colPtrSeg, rowValSeg, nzValSeg);

        return matrixSeg;
    }

    private MemorySegment createNullMatrixSegment(int m, int n) {
        val matrixSeg = ClarabelCscMatrix_f64.allocate(arena);
        val colPtrSeg = arena.allocateFrom(C_LONG_LONG, new long[n + 1]);
        clarabel_CscMatrix_f64_init(matrixSeg, m, n, colPtrSeg, NULL, NULL);

        return matrixSeg;
    }

    private MemorySegment createArraySegment(double[] array) {
        return arena.allocateFrom(C_DOUBLE, array);
    }

    private MemorySegment createNullArraySegment() {
        return arena.allocateFrom(C_DOUBLE);
    }

    private MemorySegment createConesSegment(List<Cone> cones) {
        val conesSeg = ClarabelSupportedConeT_f64.allocateArray(cones.size(), arena);

        for (int i = 0; i < cones.size(); i++) {
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

    private MemorySegment createNullConesSegment() {
        return ClarabelSupportedConeT_f64.allocateArray(0, arena);
    }

    /**
     * Sets the <a href="https://clarabel.org">Clarabel</a> solver settings.
     * <p>
     * If not called, then solver defaults are applied.
     *
     * @param parameters parameter object for the solver settings
     */
    public void setParameters(@NonNull Parameters parameters) {
        checkState(stage != Stage.NEW, "model must not be in stage new");

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
     * @return solver status
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
     * @return primal variables of this optimized {@link Model}
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
     * @return dual variables of this optimized {@link Model}
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
     * @return slack variables of this optimized {@link Model}
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
     * @return primal objective of this optimized {@link Model}
     * @see <a href="https://clarabel.org">Clarabel</a>
     */
    public double objVal() {
        checkStageIsOptimized();
        return ClarabelDefaultSolution_f64.obj_val(solutionSeg);
    }

    /**
     * @return dual objective of this optimized {@link Model}
     * @see <a href="https://clarabel.org">Clarabel</a>
     */
    public double objValDual() {
        checkStageIsOptimized();
        return ClarabelDefaultSolution_f64.obj_val_dual(solutionSeg);
    }

    /**
     * @return time needed until this {@link Model} was optimized
     * @see <a href="https://clarabel.org">Clarabel</a>
     */
    public double solveTime() {
        checkStageIsOptimized();
        return ClarabelDefaultSolution_f64.solve_time(solutionSeg);
    }

    /**
     * @return performed number of iterations until this {@link Model} was optimized
     * @see <a href="https://clarabel.org">Clarabel</a>
     */
    public int iterations() {
        checkStageIsOptimized();
        return ClarabelDefaultSolution_f64.iterations(solutionSeg);
    }

    /**
     * @return primal residual of this optimized {@link Model}
     * @see <a href="https://clarabel.org">Clarabel</a>
     */
    public double rPrim() {
        checkStageIsOptimized();
        return ClarabelDefaultSolution_f64.r_prim(solutionSeg);
    }

    /**
     * @return dual residual of this optimized {@link Model}
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
