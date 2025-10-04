package com.ustermetrics.clarabel4j;

import lombok.val;

import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.*;

/**
 * The direct linear solver used by <a href="https://clarabel.org">Clarabel</a>.
 */
public enum DirectSolveMethod {

    AUTO(AUTO()),
    QDLDL(QDLDL()),
    FAER(FAER()),
    PARDISO_MKL(PARDISO_MKL());

    private final int method;

    DirectSolveMethod(int method) {
        this.method = method;
    }

    int method() {
        return method;
    }

    static DirectSolveMethod valueOf(int method) {
        for (val c : values()) {
            if (c.method() == method) {
                return c;
            }
        }

        throw new IllegalArgumentException("Unknown direct solve method " + method);
    }

}
