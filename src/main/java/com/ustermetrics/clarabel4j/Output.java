package com.ustermetrics.clarabel4j;

/**
 * Sealed interface for output types
 *
 * @see <a href="https://clarabel.org">Clarabel</a>
 */
public sealed interface Output permits StdOutOutput, StringOutput, FileOutput {
}
