package com.ustermetrics.clarabel4j.cone;

import lombok.NonNull;

import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.ClarabelGenPowerConeT_Tag;

public record GenPowerCone(double @NonNull [] a, long n) implements Cone {

    @Override
    public int getTag() {
        return ClarabelGenPowerConeT_Tag();
    }

}
