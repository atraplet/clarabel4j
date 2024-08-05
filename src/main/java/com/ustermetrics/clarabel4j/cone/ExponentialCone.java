package com.ustermetrics.clarabel4j.cone;

import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.ClarabelExponentialConeT_Tag;

/**
 * Exponential Cone
 *
 * @see <a href="https://clarabel.org">Clarabel</a>
 */
public record ExponentialCone() implements Cone {

    @Override
    public int tag() {
        return ClarabelExponentialConeT_Tag();
    }

}
