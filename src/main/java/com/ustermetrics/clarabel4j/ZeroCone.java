package com.ustermetrics.clarabel4j;

import lombok.Getter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.ClarabelZeroConeT_Tag;

/**
 * Zero Cone
 *
 * @see <a href="https://clarabel.org">Clarabel</a>
 */
@Getter
public final class ZeroCone extends Cone {

    private final long n;

    /**
     * @param n dimension
     */
    public ZeroCone(long n) {
        checkArgument(n > 0, "n must be positive");
        this.n = n;
    }

    @Override
    int getTag() {
        return ClarabelZeroConeT_Tag();
    }

    @Override
    public long getDimension() {
        return n;
    }

}
