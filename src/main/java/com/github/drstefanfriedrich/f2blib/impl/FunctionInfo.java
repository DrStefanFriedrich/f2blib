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

import com.github.drstefanfriedrich.f2blib.ast.FunctionDefinition;
import com.github.drstefanfriedrich.f2blib.visitor.FunctionEvaluationValidator;
import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Holds internal information needed for a given function.
 */
class FunctionInfo {

    private final FunctionEvaluation functionEvaluation;
    private final String prettyPrintedFunction;
    private final FunctionDefinition functionDefinition;
    private final FunctionEvaluationValidator functionEvaluationValidator;

    public FunctionInfo(FunctionEvaluation functionEvaluation, String prettyPrintedFunction,
                        FunctionEvaluationValidator functionEvaluationValidator) {
        this.functionEvaluation = functionEvaluation;
        this.prettyPrintedFunction = prettyPrintedFunction;
        this.functionDefinition = null;
        this.functionEvaluationValidator = functionEvaluationValidator;
    }

    public FunctionInfo(FunctionDefinition functionDefinition, String prettyPrintedFunction,
                        FunctionEvaluationValidator functionEvaluationValidator) {
        this.functionEvaluation = null;
        this.prettyPrintedFunction = prettyPrintedFunction;
        this.functionDefinition = functionDefinition;
        this.functionEvaluationValidator = functionEvaluationValidator;
    }

    public FunctionEvaluation getFunctionEvaluation() {
        return functionEvaluation;
    }

    public String getPrettyPrintedFunction() {
        return prettyPrintedFunction;
    }

    public FunctionDefinition getFunctionDefinition() {
        return functionDefinition;
    }

    public FunctionEvaluationValidator getFunctionEvaluationValidator() {
        return functionEvaluationValidator;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("functionEvaluation", functionEvaluation)
                .add("prettyPrintedFunction", prettyPrintedFunction)
                .add("functionDefinition", prettyPrintedFunction)
                .add("functionEvaluationValidator", functionEvaluationValidator)
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
                prettyPrintedFunction.equals(that.prettyPrintedFunction) &&
                functionEvaluationValidator.equals(that.functionEvaluationValidator) &&
                functionDefinition.equals(that.functionDefinition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionEvaluation, prettyPrintedFunction, functionDefinition, functionEvaluationValidator);
    }

}
