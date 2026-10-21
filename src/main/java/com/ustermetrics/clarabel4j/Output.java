// SPDX-FileCopyrightText: Uster Metrics GmbH
// SPDX-License-Identifier: Apache-2.0
package com.ustermetrics.clarabel4j;

/** Sealed interface for output types */
public sealed interface Output permits StdOutOutput, StringOutput, FileOutput {}
