package com.ustermetrics.clarabel4j.cone;

public sealed interface Cone permits ZeroCone, NonnegativeCone, SecondOrderCone, ExponentialCone, PowerCone,
        GenPowerCone {

    int getTag();

}
