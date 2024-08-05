package com.ustermetrics.clarabel4j.cone;

/**
 * Cone interface permits supported cone types
 *
 * @see <a href="https://clarabel.org">Clarabel</a>
 */
public sealed interface Cone
        permits ZeroCone, NonnegativeCone, SecondOrderCone, ExponentialCone, PowerCone, GenPowerCone {

    int tag();

}
