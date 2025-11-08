package com.ustermetrics.clarabel4j;

import com.ustermetrics.clarabel4j.bindings.*;
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
 * To control the lifecycle of native memory, {@link Model} implements the {@link AutoCloseable} interface and should
 * be used with the <i>try-with-resources</i> statement or the {@link #close()} method needs to be called manually.
 */
public class Model implements AutoCloseable {

    private enum Stage {NEW, SETUP, OPTIMIZED}

    private final Arena arena;
    private final boolean closeArena;
    private Stage stage = Stage.NEW;
    private Parameters parameters;
    private Output output;
    private MemorySegment solverSeg;
    private MemorySegment solutionSeg;
    private MemorySegment infoSeg;

    /**
     * Creates a new {@link Model} instance, where the lifecycle of native memory is controlled by a new confined arena.
     * The arena is closed when the {@link Model} instance is closed.
     */
    public Model() {
        arena = Arena.ofConfined();
        closeArena = true;
    }

    /**
     * Creates a new {@link Model} instance, where the lifecycle of native memory is controlled by the given
     * {@link Arena} instance.
     *
     * @param arena {@link Arena} instance to control the lifecycle of native memory
     */
    public Model(Arena arena) {
        this.arena = arena;
        closeArena = false;
    }

    /**
     * Sets the <a href="https://clarabel.org">Clarabel</a> solver settings.
     * <p>
     * If not called, then solver defaults are applied.
     *
     * @param parameters parameter object for the solver settings
     */
    public void setParameters(@NonNull Parameters parameters) {
        checkState(stage == Stage.NEW, "model must be in stage new");

        this.parameters = parameters;
    }

    /**
     * Sets the output type.
     * If not called, then the output goes to standard out.
     *
     * @param output the output type
     */
    public void setOutput(@NonNull Output output) {
        checkState(stage == Stage.NEW, "model must be in stage new");

        this.output = output;
    }

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

        val pSeg = p != null ? createMatrixSegment(p) : createNullMatrixSegment(a.n(), a.n());
        val qSeg = q != null ? createArraySegment(q) : createNullArraySegment(p != null ? p.n() : a.n());
        val aSeg = a != null ? createMatrixSegment(a) : createNullMatrixSegment(0, p.n());
        val bSeg = b != null ? createArraySegment(b) : createNullArraySegment(0);
        val nCones = cones != null ? cones.size() : 0;
        val conesSeg = cones != null ? createConesSegment(cones) : createNullConesSegment();
        val settingsSeg = createSettingsSegment();

        solverSeg = clarabel_DefaultSolver_f64_new(pSeg, qSeg, aSeg, bSeg, nCones, conesSeg, settingsSeg);

        setOutput();

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

    private MemorySegment createNullArraySegment(int length) {
        val array = new double[length];
        return createArraySegment(array);
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

    private MemorySegment createSettingsSegment() {
        val settingsSeg = clarabel_DefaultSettings_f64_default(arena);

        if (parameters != null) {
            Optional.ofNullable(parameters.maxIter())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.max_iter(settingsSeg, p));
            Optional.ofNullable(parameters.timeLimit())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.time_limit(settingsSeg, p));
            Optional.ofNullable(parameters.verbose())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.verbose(settingsSeg, p));
            Optional.ofNullable(parameters.maxStepFraction())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.max_step_fraction(settingsSeg, p));
            Optional.ofNullable(parameters.tolGapAbs())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.tol_gap_abs(settingsSeg, p));
            Optional.ofNullable(parameters.tolGapRel())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.tol_gap_rel(settingsSeg, p));
            Optional.ofNullable(parameters.tolFeas())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.tol_feas(settingsSeg, p));
            Optional.ofNullable(parameters.tolInfeasAbs())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.tol_infeas_abs(settingsSeg, p));
            Optional.ofNullable(parameters.tolInfeasRel())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.tol_infeas_rel(settingsSeg, p));
            Optional.ofNullable(parameters.tolKtratio())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.tol_ktratio(settingsSeg, p));
            Optional.ofNullable(parameters.reducedTolGapAbs())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.reduced_tol_gap_abs(settingsSeg, p));
            Optional.ofNullable(parameters.reducedTolGapRel())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.reduced_tol_gap_rel(settingsSeg, p));
            Optional.ofNullable(parameters.reducedTolFeas())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.reduced_tol_feas(settingsSeg, p));
            Optional.ofNullable(parameters.reducedTolInfeasAbs())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.reduced_tol_infeas_abs(settingsSeg, p));
            Optional.ofNullable(parameters.reducedTolInfeasRel())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.reduced_tol_infeas_rel(settingsSeg, p));
            Optional.ofNullable(parameters.reducedTolKtratio())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.reduced_tol_ktratio(settingsSeg, p));
            Optional.ofNullable(parameters.equilibrateEnable())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.equilibrate_enable(settingsSeg, p));
            Optional.ofNullable(parameters.equilibrateMaxIter())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.equilibrate_max_iter(settingsSeg, p));
            Optional.ofNullable(parameters.equilibrateMinScaling())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.equilibrate_min_scaling(settingsSeg, p));
            Optional.ofNullable(parameters.equilibrateMaxScaling())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.equilibrate_max_scaling(settingsSeg, p));
            Optional.ofNullable(parameters.linesearchBacktrackStep())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.linesearch_backtrack_step(settingsSeg, p));
            Optional.ofNullable(parameters.minSwitchStepLength())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.min_switch_step_length(settingsSeg, p));
            Optional.ofNullable(parameters.minTerminateStepLength())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.min_terminate_step_length(settingsSeg, p));
            Optional.ofNullable(parameters.maxThreads())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.max_threads(settingsSeg, p));
            Optional.ofNullable(parameters.directKktSolver())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.direct_kkt_solver(settingsSeg, p));
            Optional.ofNullable(parameters.directSolveMethod())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.direct_solve_method(settingsSeg, p.method()));
            Optional.ofNullable(parameters.staticRegularizationEnable())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.static_regularization_enable(settingsSeg, p));
            Optional.ofNullable(parameters.staticRegularizationConstant())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.static_regularization_constant(settingsSeg, p));
            Optional.ofNullable(parameters.staticRegularizationProportional())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.static_regularization_proportional(settingsSeg, p));
            Optional.ofNullable(parameters.dynamicRegularizationEnable())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.dynamic_regularization_enable(settingsSeg, p));
            Optional.ofNullable(parameters.dynamicRegularizationEps())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.dynamic_regularization_eps(settingsSeg, p));
            Optional.ofNullable(parameters.dynamicRegularizationDelta())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.dynamic_regularization_delta(settingsSeg, p));
            Optional.ofNullable(parameters.iterativeRefinementEnable())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.iterative_refinement_enable(settingsSeg, p));
            Optional.ofNullable(parameters.iterativeRefinementReltol())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.iterative_refinement_reltol(settingsSeg, p));
            Optional.ofNullable(parameters.iterativeRefinementAbstol())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.iterative_refinement_abstol(settingsSeg, p));
            Optional.ofNullable(parameters.iterativeRefinementMaxIter())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.iterative_refinement_max_iter(settingsSeg, p));
            Optional.ofNullable(parameters.iterativeRefinementStopRatio())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.iterative_refinement_stop_ratio(settingsSeg, p));
            Optional.ofNullable(parameters.presolveEnable())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.presolve_enable(settingsSeg, p));
            Optional.ofNullable(parameters.pardisoIparm())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.pardiso_iparm(settingsSeg, arena.allocateFrom(C_INT, p)));
            Optional.ofNullable(parameters.pardisoVerbose())
                    .ifPresent(p -> ClarabelDefaultSettings_f64.pardiso_verbose(settingsSeg, p));
        }

        return settingsSeg;
    }

    private void setOutput() {
        if (output != null) {
            switch (output) {
                case StdOutOutput _ -> clarabel_DefaultSolver_f64_print_to_stdout(solverSeg);
                case StringOutput _ -> clarabel_DefaultSolver_f64_print_to_buffer(solverSeg);
                case FileOutput fileOutput ->
                        clarabel_DefaultSolver_f64_print_to_file(solverSeg, arena.allocateFrom(fileOutput.getName()));
            }
        }
    }

    /**
     * Optimizes this {@link Model} with the<a href="https://clarabel.org">Clarabel</a> solver.
     *
     * @return solver status
     */
    public Status optimize() {
        checkState(stage != Stage.NEW, "model must not be in stage new");

        clarabel_DefaultSolver_f64_solve(solverSeg);
        solutionSeg = ClarabelDefaultSolution_f64.reinterpret(clarabel_DefaultSolver_f64_solution(arena, solverSeg),
                arena, null);
        infoSeg = ClarabelDefaultInfo_f64.reinterpret(clarabel_DefaultSolver_f64_info(arena, solverSeg), arena, null);

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

    /**
     * @return direct solve method that was used for this optimized {@link Model}
     * @see <a href="https://clarabel.org">Clarabel</a>
     */
    public DirectSolveMethod directSolveMethod() {
        checkStageIsOptimized();
        return DirectSolveMethod.valueOf(ClarabelLinearSolverInfo.name(ClarabelDefaultInfo_f64.linsolver(infoSeg)));
    }

    /**
     * @return number of threads that was used by the solver for this optimized {@link Model}
     * @see <a href="https://clarabel.org">Clarabel</a>
     */
    public int threads() {
        checkStageIsOptimized();
        return ClarabelLinearSolverInfo.threads(ClarabelDefaultInfo_f64.linsolver(infoSeg));
    }

    /**
     * @return number of nonzeros in the linear system of this optimized {@link Model}
     * @see <a href="https://clarabel.org">Clarabel</a>
     */
    public int nnzA() {
        checkStageIsOptimized();
        return ClarabelLinearSolverInfo.nnzA(ClarabelDefaultInfo_f64.linsolver(infoSeg));
    }

    /**
     * @return number of nonzeros in the factored system of this optimized {@link Model}
     * @see <a href="https://clarabel.org">Clarabel</a>
     */
    public int nnzL() {
        checkStageIsOptimized();
        return ClarabelLinearSolverInfo.nnzL(ClarabelDefaultInfo_f64.linsolver(infoSeg));
    }

    /**
     * @return string output of this optimized {@link Model}
     */
    public String getStringOutput() {
        checkStageIsOptimized();
        checkState(output instanceof StringOutput, "output must be string");

        val bufferSeg = clarabel_DefaultSolver_f64_get_print_buffer(solverSeg);
        val output = bufferSeg.getString(0);
        clarabel_free_print_buffer(bufferSeg);

        return output;
    }

    private void checkStageIsOptimized() {
        checkState(stage == Stage.OPTIMIZED, "model must be in stage optimized");
    }

    @Override
    public void close() {
        if (stage != Stage.NEW) {
            clarabel_DefaultSolver_f64_free(solverSeg);
        }
        if (closeArena) {
            arena.close();
        }
    }

}
