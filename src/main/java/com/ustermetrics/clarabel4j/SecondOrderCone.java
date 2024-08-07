package com.ustermetrics.clarabel4j;

import lombok.Getter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.ClarabelSecondOrderConeT_Tag;

/**
 * Second-Order Cone
 *
 * @see <a href="https://clarabel.org">Clarabel</a>
 */
@Getter
public final class SecondOrderCone extends Cone {

    private final long n;

    /**
     * @param n dimension
     */
    public SecondOrderCone(long n) {
        checkArgument(n > 0, "n must be positive");
        this.n = n;
    }

    @Override
    int getTag() {
        return ClarabelSecondOrderConeT_Tag();
    }

}
