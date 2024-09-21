package com.ustermetrics.clarabel4j;

/**
 * Abstract sealed class {@code Cone} permits supported cone types.
 *
 * @see <a href="https://clarabel.org">Clarabel</a>
 */
public abstract sealed class Cone
        permits ZeroCone, NonnegativeCone, SecondOrderCone, ExponentialCone, PowerCone, GenPowerCone {

    abstract int getTag();

    abstract long getDimension();

}
