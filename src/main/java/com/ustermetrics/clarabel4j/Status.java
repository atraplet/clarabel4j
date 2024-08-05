package com.ustermetrics.clarabel4j;

import lombok.val;

import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.*;

/**
 * The <a href="https://clarabel.org">Clarabel</a> solver status from optimizing a {@link Model}.
 */
public enum Status {

    UNSOLVED(ClarabelUnsolved()),
    SOLVED(ClarabelSolved()),
    PRIMAL_INFEASIBLE(ClarabelPrimalInfeasible()),
    DUAL_INFEASIBLE(ClarabelDualInfeasible()),
    ALMOST_SOLVED(ClarabelAlmostSolved()),
    ALMOST_PRIMAL_INFEASIBLE(ClarabelAlmostPrimalInfeasible()),
    ALMOST_DUAL_INFEASIBLE(ClarabelAlmostDualInfeasible()),
    MAX_ITERATIONS(ClarabelMaxIterations()),
    MAX_TIME(ClarabelMaxTime()),
    NUMERICAL_ERROR(ClarabelNumericalError()),
    INSUFFICIENT_PROGRESS(ClarabelInsufficientProgress());

    private final int status;

    Status(int status) {
        this.status = status;
    }

    private int status() {
        return status;
    }

    static Status valueOf(int status) {
        for (val c : values()) {
            if (c.status() == status) {
                return c;
            }
        }

        throw new IllegalArgumentException("Unknown status " + status);
    }

}
