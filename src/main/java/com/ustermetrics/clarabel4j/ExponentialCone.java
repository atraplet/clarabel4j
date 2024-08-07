package com.ustermetrics.clarabel4j;

import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.ClarabelExponentialConeT_Tag;

/**
 * Exponential Cone
 *
 * @see <a href="https://clarabel.org">Clarabel</a>
 */
public final class ExponentialCone extends Cone {

    @Override
    int getTag() {
        return ClarabelExponentialConeT_Tag();
    }

}
