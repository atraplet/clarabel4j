package com.ustermetrics.clarabel4j.cone;

import static com.google.common.base.Preconditions.checkArgument;
import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.ClarabelNonnegativeConeT_Tag;

/**
 * Nonnegative Orthant
 *
 * @param n dimension
 * @see <a href="https://clarabel.org">Clarabel</a>
 */
public record NonnegativeCone(long n) implements Cone {

    public NonnegativeCone {
        checkArgument(n > 0, "n must be positive");
    }

    @Override
    public int tag() {
        return ClarabelNonnegativeConeT_Tag();
    }

}
