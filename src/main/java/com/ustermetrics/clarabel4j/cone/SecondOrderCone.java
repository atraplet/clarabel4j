package com.ustermetrics.clarabel4j.cone;

import static com.ustermetrics.clarabel4j.cone.ConeType.SECOND_ORDER_CONE;

public class SecondOrderCone extends ConeWithDimension {

    public SecondOrderCone(long dimension) {
        super(SECOND_ORDER_CONE, dimension);
    }

}
