package com.ustermetrics.clarabel4j.cone;

import static com.ustermetrics.clarabel4j.bindings.Clarabel_h.ClarabelNonnegativeConeT_Tag;

public record NonnegativeCone(long n) implements Cone {

    @Override
    public int getTag() {
        return ClarabelNonnegativeConeT_Tag();
    }

}
