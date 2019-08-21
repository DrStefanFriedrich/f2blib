/*****************************************************************************
 *
 * Copyright (c) 2019 Stefan Friedrich
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package com.github.drstefanfriedrich.f2blib.impl;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Holds internal information needed for a given function.
 */
class FunctionInfo {

    private final FunctionEvaluation functionEvaluation;

    private final String prettyPrintedFunction;

    public FunctionInfo(FunctionEvaluation functionEvaluation, String prettyPrintedFunction) {
        this.functionEvaluation = functionEvaluation;
        this.prettyPrintedFunction = prettyPrintedFunction;
    }

    public FunctionEvaluation getFunctionEvaluation() {
        return functionEvaluation;
    }

    public String getPrettyPrintedFunction() {
        return prettyPrintedFunction;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("functionEvaluation", functionEvaluation)
                .add("prettyPrintedFunction", prettyPrintedFunction)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FunctionInfo that = (FunctionInfo) o;
        return functionEvaluation.equals(that.functionEvaluation) &&
                prettyPrintedFunction.equals(that.prettyPrintedFunction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionEvaluation, prettyPrintedFunction);
    }

}
