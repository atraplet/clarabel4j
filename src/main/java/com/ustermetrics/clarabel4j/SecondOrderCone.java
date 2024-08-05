package com.ustermetrics.clarabel4j;

import static com.google.common.base.Preconditions.checkArgument;
import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.ClarabelSecondOrderConeT_Tag;

/**
 * Second-Order Cone
 *
 * @param n dimension
 * @see <a href="https://clarabel.org">Clarabel</a>
 */
public record SecondOrderCone(long n) implements Cone {

    public SecondOrderCone {
        checkArgument(n > 0, "n must be positive");
    }

    @Override
    public int tag() {
        return ClarabelSecondOrderConeT_Tag();
    }

}
