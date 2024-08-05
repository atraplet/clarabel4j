package com.ustermetrics.clarabel4j.cone;

import static com.google.common.base.Preconditions.checkArgument;
import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.ClarabelZeroConeT_Tag;

/**
 * Zero Cone
 *
 * @param n dimension
 * @see <a href="https://clarabel.org">Clarabel</a>
 */
public record ZeroCone(long n) implements Cone {

    public ZeroCone {
        checkArgument(n > 0, "n must be positive");
    }

    @Override
    public int tag() {
        return ClarabelZeroConeT_Tag();
    }

}
