package com.ustermetrics.clarabel4j.cone;

import lombok.val;

import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.*;

public enum ConeType {

    ZERO_CONE(ClarabelZeroConeT_Tag()),
    NONNEGATIVE_CONE(ClarabelNonnegativeConeT_Tag()),
    SECOND_ORDER_CONE(ClarabelSecondOrderConeT_Tag()),
    EXPONENTIAL_CONE(ClarabelExponentialConeT_Tag()),
    POWER_CONE(ClarabelPowerConeT_Tag()),
    GENERALIZED_POWER_CONE(ClarabelGenPowerConeT_Tag());

    private final int tag;

    ConeType(int tag) {
        this.tag = tag;
    }

    private int tag() {
        return tag;
    }

    static ConeType valueOf(int tag) {
        for (val c : values()) {
            if (c.tag() == tag) {
                return c;
            }
        }

        throw new IllegalArgumentException("Unknown tag " + tag);
    }

}
