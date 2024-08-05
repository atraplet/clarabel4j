package com.ustermetrics.clarabel4j.cone;

import static com.google.common.base.Preconditions.checkArgument;
import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.ClarabelPowerConeT_Tag;

/**
 * Power Cone
 *
 * @param a power defining the cone
 * @see <a href="https://clarabel.org">Clarabel</a>
 */
public record PowerCone(double a) implements Cone {

    public PowerCone {
        checkArgument(0 < a && a < 1, "a must be in (0, 1)");
    }

    @Override
    public int getTag() {
        return ClarabelPowerConeT_Tag();
    }

}
