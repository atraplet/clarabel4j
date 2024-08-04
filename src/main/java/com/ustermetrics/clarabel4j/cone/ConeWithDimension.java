package com.ustermetrics.clarabel4j.cone;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static lombok.AccessLevel.PROTECTED;

@Getter
@AllArgsConstructor(access = PROTECTED)
public abstract class ConeWithDimension implements Cone {

    private final ConeType coneType;
    private final long dimension;

}
