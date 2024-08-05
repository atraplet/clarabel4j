package com.ustermetrics.clarabel4j.cone;

import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.ClarabelPowerConeT_Tag;

public record PowerCone(double a) implements Cone {

    @Override
    public int getTag() {
        return ClarabelPowerConeT_Tag();
    }

}
