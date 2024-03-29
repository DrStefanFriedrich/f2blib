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

package com.github.drstefanfriedrich.f2blib.ast;

import com.github.drstefanfriedrich.f2blib.visitor.Visitor;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Objects;

/**
 * A {@link ForLoop} models a very simple for loop.
 */
public final class ForLoop implements Serializable, ASTElement {

    private final String variableName;

    private final IntExpression start;

    private final IntExpression end;

    private final IntExpression step;

    private final FunctionsWrapper functionsWrapper;

    public ForLoop(String variableName, IntExpression start, IntExpression end, IntExpression step,
                   FunctionsWrapper functionsWrapper) {
        this.variableName = variableName;
        this.start = start;
        this.end = end;
        this.step = step;
        this.functionsWrapper = functionsWrapper;
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public <T> T accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public <T> T acceptStart(Visitor visitor) {
        return start.accept(visitor);
    }

    public <T> T acceptEnd(Visitor visitor) {
        return end.accept(visitor);
    }

    public <T> T acceptStep(Visitor visitor) {
        return step.accept(visitor);
    }

    public <T> T acceptFunctionsWrapper(Visitor visitor) {
        return functionsWrapper.accept(visitor);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("variableName", variableName)
                .add("start", start)
                .add("end", end)
                .add("step", step)
                .add("functionsWrapper", functionsWrapper)
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
        ForLoop forLoop = (ForLoop) o;
        return Objects.equals(variableName, forLoop.variableName) &&
                Objects.equals(start, forLoop.start) &&
                Objects.equals(end, forLoop.end) &&
                Objects.equals(step, forLoop.step) &&
                Objects.equals(functionsWrapper, forLoop.functionsWrapper);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variableName, start, end, step, functionsWrapper);
    }

}
