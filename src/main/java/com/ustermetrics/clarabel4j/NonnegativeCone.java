package com.ustermetrics.clarabel4j;

import lombok.Getter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.ClarabelNonnegativeConeT_Tag;

/**
 * Nonnegative Orthant
 *
 * @see <a href="https://clarabel.org">Clarabel</a>
 */
@Getter
public final class NonnegativeCone extends Cone {

    private final long n;

    /**
     * @param n dimension
     */
    public NonnegativeCone(long n) {
        checkArgument(n > 0, "n must be positive");
        this.n = n;
    }

    @Override
    int getTag() {
        return ClarabelNonnegativeConeT_Tag();
    }

    @Override
    public long getDimension() {
        return n;
    }

}
