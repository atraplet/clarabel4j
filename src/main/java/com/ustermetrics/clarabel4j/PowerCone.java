package com.ustermetrics.clarabel4j;

import lombok.Getter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.ClarabelPowerConeT_Tag;

/**
 * Power Cone
 *
 * @see <a href="https://clarabel.org">Clarabel</a>
 */
@Getter
public final class PowerCone extends Cone {

    private final double a;

    /**
     * @param a power defining the cone
     */
    public PowerCone(double a) {
        checkArgument(0 < a && a < 1, "a must be in (0, 1)");
        this.a = a;
    }

    @Override
    int getTag() {
        return ClarabelPowerConeT_Tag();
    }

    @Override
    public long getDimension() {
        return 3;
    }

}
