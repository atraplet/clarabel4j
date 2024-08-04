package com.ustermetrics.clarabel4j.cone;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.ustermetrics.clarabel4j.cone.ConeType.POWER_CONE;

@Getter
@AllArgsConstructor
public class PowerCone implements Cone {

    private final double a;

    @Override
    public ConeType getConeType() {
        return POWER_CONE;
    }

}
