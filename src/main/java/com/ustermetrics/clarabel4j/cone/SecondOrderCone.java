package com.ustermetrics.clarabel4j.cone;

import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.ClarabelSecondOrderConeT_Tag;

public record SecondOrderCone(long n) implements Cone {

    @Override
    public int getTag() {
        return ClarabelSecondOrderConeT_Tag();
    }

}
