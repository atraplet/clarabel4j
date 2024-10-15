package com.ustermetrics.clarabel4j;

import lombok.Builder;
import lombok.val;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A parameter object for <a href="https://clarabel.org">Clarabel</a> solver settings.
 * <p>
 * If {@link Model#setParameters(Parameters parameters)} is not called, then solver defaults are applied.
 *
 * @param maxIter                          maximum number of iterations
 * @param timeLimit                        maximum run time (seconds)
 * @param verbose                          verbose printing
 * @param maxStepFraction                  maximum interior point step length
 * @param tolGapAbs                        absolute duality gap tolerance
 * @param tolGapRel                        relative duality gap tolerance
 * @param tolFeas                          feasibility check tolerance (primal and dual)
 * @param tolInfeasAbs                     absolute infeasibility tolerance (primal and dual)
 * @param tolInfeasRel                     relative infeasibility tolerance (primal and dual)
 * @param tolKtratio                       kappa/tau tolerance
 * @param reducedTolGapAbs                 reduced absolute duality gap tolerance
 * @param reducedTolGapRel                 reduced relative duality gap tolerance
 * @param reducedTolFeas                   reduced feasibility check tolerance (primal and dual)
 * @param reducedTolInfeasAbs              reduced absolute infeasibility tolerance (primal and dual)
 * @param reducedTolInfeasRel              reduced relative infeasibility tolerance (primal and dual)
 * @param reducedTolKtratio                reduced kappa/tau tolerance
 * @param equilibrateEnable                enable data equilibration pre-scaling
 * @param equilibrateMaxIter               maximum equilibration scaling iterations
 * @param equilibrateMinScaling            minimum equilibration scaling allowed
 * @param equilibrateMaxScaling            maximum equilibration scaling allowed
 * @param linesearchBacktrackStep          linesearch backtracking
 * @param minSwitchStepLength              minimum step size allowed for asymmetric cones with PrimalDual scaling
 * @param minTerminateStepLength           minimum step size allowed for symmetric cones & asymmetric cones with Dual
 *                                         scaling
 * @param directKktSolver                  use a direct linear solver method
 * @param directSolveMethod                direct linear solver
 * @param staticRegularizationEnable       enable KKT static regularization
 * @param staticRegularizationConstant     KKT static regularization parameter
 * @param staticRegularizationProportional additional regularization parameter w.r.t. the maximum abs diagonal term
 * @param dynamicRegularizationEnable      enable KKT dynamic regularization
 * @param dynamicRegularizationEps         KKT dynamic regularization threshold
 * @param dynamicRegularizationDelta       KKT dynamic regularization shift
 * @param iterativeRefinementEnable        KKT direct solve with iterative refinement
 * @param iterativeRefinementReltol        iterative refinement relative tolerance
 * @param iterativeRefinementAbstol        iterative refinement absolute tolerance
 * @param iterativeRefinementMaxIter       iterative refinement maximum iterations
 * @param iterativeRefinementStopRatio     iterative refinement stalling tolerance
 * @param presolveEnable                   enable presolve constraint reduction
 * @see <a href="https://clarabel.org">Clarabel</a>
 */
@Builder
public record Parameters(Integer maxIter, Double timeLimit, Boolean verbose, Double maxStepFraction, Double tolGapAbs,
                         Double tolGapRel, Double tolFeas, Double tolInfeasAbs, Double tolInfeasRel, Double tolKtratio,
                         Double reducedTolGapAbs, Double reducedTolGapRel, Double reducedTolFeas,
                         Double reducedTolInfeasAbs, Double reducedTolInfeasRel, Double reducedTolKtratio,
                         Boolean equilibrateEnable, Integer equilibrateMaxIter, Double equilibrateMinScaling,
                         Double equilibrateMaxScaling, Double linesearchBacktrackStep, Double minSwitchStepLength,
                         Double minTerminateStepLength, Boolean directKktSolver, DirectSolveMethod directSolveMethod,
                         Boolean staticRegularizationEnable, Double staticRegularizationConstant,
                         Double staticRegularizationProportional, Boolean dynamicRegularizationEnable,
                         Double dynamicRegularizationEps, Double dynamicRegularizationDelta,
                         Boolean iterativeRefinementEnable, Double iterativeRefinementReltol,
                         Double iterativeRefinementAbstol, Integer iterativeRefinementMaxIter,
                         Double iterativeRefinementStopRatio, Boolean presolveEnable) {

    public Parameters {
        val errMsg = "%s must be positive";
        checkArgument(maxIter == null || maxIter > 0, errMsg, "maxIter");
        checkArgument(timeLimit == null || timeLimit > 0., errMsg, "timeLimit");
        checkArgument(maxStepFraction == null || maxStepFraction > 0., errMsg, "maxStepFraction");
        checkArgument(tolGapAbs == null || tolGapAbs > 0., errMsg, "tolGapAbs");
        checkArgument(tolGapRel == null || tolGapRel > 0., errMsg, "tolGapRel");
        checkArgument(tolFeas == null || tolFeas > 0., errMsg, "tolFeas");
        checkArgument(tolInfeasAbs == null || tolInfeasAbs > 0., errMsg, "tolInfeasAbs");
        checkArgument(tolInfeasRel == null || tolInfeasRel > 0., errMsg, "tolInfeasRel");
        checkArgument(tolKtratio == null || tolKtratio > 0., errMsg, "tolKtratio");
        checkArgument(reducedTolGapAbs == null || reducedTolGapAbs > 0., errMsg, "reducedTolGapAbs");
        checkArgument(reducedTolGapRel == null || reducedTolGapRel > 0., errMsg, "reducedTolGapRel");
        checkArgument(reducedTolFeas == null || reducedTolFeas > 0., errMsg, "reducedTolFeas");
        checkArgument(reducedTolInfeasAbs == null || reducedTolInfeasAbs > 0., errMsg, "reducedTolInfeasAbs");
        checkArgument(reducedTolInfeasRel == null || reducedTolInfeasRel > 0., errMsg, "reducedTolInfeasRel");
        checkArgument(reducedTolKtratio == null || reducedTolKtratio > 0., errMsg, "reducedTolKtratio");
        checkArgument(equilibrateMaxIter == null || equilibrateMaxIter > 0, errMsg, "equilibrateMaxIter");
        checkArgument(equilibrateMinScaling == null || equilibrateMinScaling > 0., errMsg, "equilibrateMinScaling");
        checkArgument(equilibrateMaxScaling == null || equilibrateMaxScaling > 0., errMsg, "equilibrateMaxScaling");
        checkArgument(linesearchBacktrackStep == null || linesearchBacktrackStep > 0., errMsg,
                "linesearchBacktrackStep");
        checkArgument(minSwitchStepLength == null || minSwitchStepLength > 0., errMsg, "minSwitchStepLength");
        checkArgument(minTerminateStepLength == null || minTerminateStepLength > 0., errMsg, "minTerminateStepLength");
        checkArgument(staticRegularizationConstant == null || staticRegularizationConstant > 0., errMsg,
                "staticRegularizationConstant");
        checkArgument(staticRegularizationProportional == null || staticRegularizationProportional > 0., errMsg,
                "staticRegularizationProportional");
        checkArgument(dynamicRegularizationEps == null || dynamicRegularizationEps > 0., errMsg,
                "dynamicRegularizationEps");
        checkArgument(dynamicRegularizationDelta == null || dynamicRegularizationDelta > 0., errMsg,
                "dynamicRegularizationDelta");
        checkArgument(iterativeRefinementReltol == null || iterativeRefinementReltol > 0., errMsg,
                "iterativeRefinementReltol");
        checkArgument(iterativeRefinementAbstol == null || iterativeRefinementAbstol > 0., errMsg,
                "iterativeRefinementAbstol");
        checkArgument(iterativeRefinementMaxIter == null || iterativeRefinementMaxIter > 0, errMsg,
                "iterativeRefinementMaxIter");
        checkArgument(iterativeRefinementStopRatio == null || iterativeRefinementStopRatio > 0., errMsg,
                "iterativeRefinementStopRatio");
    }

}
