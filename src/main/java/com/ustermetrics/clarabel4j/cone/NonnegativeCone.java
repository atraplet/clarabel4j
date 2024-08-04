package com.ustermetrics.clarabel4j.cone;

import static com.ustermetrics.clarabel4j.cone.ConeType.NONNEGATIVE_CONE;

public class NonnegativeCone extends ConeWithDimension {

    public NonnegativeCone(long dimension) {
        super(NONNEGATIVE_CONE, dimension);
    }

}
