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

import java.util.Objects;

/**
 * Common base class for {@link Sum} and {@link Prod}.
 */
public abstract class AbstractSumProduct implements Expression, IntExpression {

    private final Expression inner;

    private final String variableName;

    private final IntExpression start;

    private final IntExpression end;

    public AbstractSumProduct(Expression inner, String variableName, IntExpression start, IntExpression end) {
        this.inner = inner;
        this.variableName = variableName;
        this.start = start;
        this.end = end;
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("inner", inner)
                .add("variableName", variableName)
                .add("start", start)
                .add("end", end)
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
        AbstractSumProduct that = (AbstractSumProduct) o;
        return inner.equals(that.inner) &&
                variableName.equals(that.variableName) &&
                start.equals(that.start) &&
                end.equals(that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inner, variableName, start, end);
    }

    @Override
    public boolean evaluatesToDouble() {
        return inner.evaluatesToDouble();
    }

    public <T> T acceptInner(Visitor visitor) {
        return inner.accept(visitor);
    }

    public <T> T acceptStart(Visitor visitor) {
        return start.accept(visitor);
    }

    public <T> T acceptEnd(Visitor visitor) {
        return end.accept(visitor);
    }

}
