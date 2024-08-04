package com.ustermetrics.clarabel4j.cone;

import static com.ustermetrics.clarabel4j.cone.ConeType.EXPONENTIAL_CONE;

public class ExponentialCone implements Cone {

    @Override
    public ConeType getConeType() {
        return EXPONENTIAL_CONE;
    }

}
