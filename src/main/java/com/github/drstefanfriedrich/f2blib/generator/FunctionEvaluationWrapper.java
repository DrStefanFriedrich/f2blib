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

package com.github.drstefanfriedrich.f2blib.generator;

import com.github.drstefanfriedrich.f2blib.impl.FunctionEvaluation;
import com.github.drstefanfriedrich.f2blib.visitor.FunctionEvaluationValidator;
import com.google.common.base.MoreObjects;

import java.util.Objects;

public class FunctionEvaluationWrapper {

    private final FunctionEvaluation functionEvaluation;
    private final FunctionEvaluationValidator functionEvaluationValidator;

    public FunctionEvaluationWrapper(FunctionEvaluation functionEvaluation, FunctionEvaluationValidator functionEvaluationValidator) {
        this.functionEvaluation = functionEvaluation;
        this.functionEvaluationValidator = functionEvaluationValidator;
    }

    public FunctionEvaluation getFunctionEvaluation() {
        return functionEvaluation;
    }

    public FunctionEvaluationValidator getFunctionEvaluationValidator() {
        return functionEvaluationValidator;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("functionEvaluation", functionEvaluation)
                .add("functionEvaluationValidator", functionEvaluationValidator)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionEvaluationWrapper that = (FunctionEvaluationWrapper) o;
        return functionEvaluation.equals(that.functionEvaluation) &&
                functionEvaluationValidator.equals(that.functionEvaluationValidator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionEvaluation, functionEvaluationValidator);
    }

}
