package com.ustermetrics.clarabel4j.cone;

import lombok.NonNull;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.ClarabelGenPowerConeT_Tag;
import static java.lang.Math.abs;

/**
 * Generalized Power Cone
 *
 * @param a power defining the cone
 * @param n dimension
 * @see <a href="https://clarabel.org">Clarabel</a>
 */
public record GenPowerCone(double @NonNull [] a, long n) implements Cone {

    private static final double TOLERANCE = Math.ulp(1.0);

    public GenPowerCone {
        checkArgument(a.length > 0, "Length of a must be positive");
        checkArgument(Arrays.stream(a).allMatch(e -> 0 < e && e < 1), "All elements of a must be in (0, 1)");
        checkArgument(abs(Arrays.stream(a).sum() - 1.0) < TOLERANCE,
                "The sum of all elements of a must be equal to one");
        checkArgument(n > 0, "n must be positive");
    }

    @Override
    public int getTag() {
        return ClarabelGenPowerConeT_Tag();
    }

}
