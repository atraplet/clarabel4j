package com.ustermetrics.clarabel4j.cone;

import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.ClarabelZeroConeT_Tag;

public record ZeroCone(long n) implements Cone {

    @Override
    public int getTag() {
        return ClarabelZeroConeT_Tag();
    }

}
