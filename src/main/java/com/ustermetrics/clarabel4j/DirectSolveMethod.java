package com.ustermetrics.clarabel4j;

import lombok.val;

import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.QDLDL;

/**
 * The direct linear solver used by <a href="https://clarabel.org">Clarabel</a>.
 */
public enum DirectSolveMethod {

    QDLDL(QDLDL());

    private final int method;

    DirectSolveMethod(int method) {
        this.method = method;
    }

    private int method() {
        return method;
    }

    static DirectSolveMethod valueOf(int method) {
        for (val c : values()) {
            if (c.method() == method) {
                return c;
            }
        }

        throw new IllegalArgumentException("Unknown method " + method);
    }

}
