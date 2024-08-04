package com.ustermetrics.clarabel4j.cone;

import lombok.Getter;

import static com.ustermetrics.clarabel4j.cone.ConeType.GENERALIZED_POWER_CONE;

@Getter
public class GeneralizedPowerCone extends ConeWithDimension {

    private final double[] a;

    public GeneralizedPowerCone(double[] a, long dimension) {
        super(GENERALIZED_POWER_CONE, dimension);
        this.a = a;
    }

}
