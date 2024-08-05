package com.ustermetrics.clarabel4j.cone;

import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.ClarabelExponentialConeT_Tag;

public record ExponentialCone() implements Cone {

    @Override
    public int getTag() {
        return ClarabelExponentialConeT_Tag();
    }

}
