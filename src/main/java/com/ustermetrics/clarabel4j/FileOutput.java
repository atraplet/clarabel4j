package com.ustermetrics.clarabel4j;

import lombok.Getter;
import lombok.NonNull;

/**
 * File output
 *
 * @see <a href="https://clarabel.org">Clarabel</a>
 */
@Getter
public final class FileOutput implements Output {

    private final String name;

    public FileOutput(@NonNull String name) {
        this.name = name;
    }

}
