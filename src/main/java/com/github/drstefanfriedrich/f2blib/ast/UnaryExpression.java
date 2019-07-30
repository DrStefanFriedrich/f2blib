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

import com.google.common.base.MoreObjects;
import com.github.drstefanfriedrich.f2blib.visitor.Visitor;

import java.util.Objects;

/**
 * An expression that evaluates only one sub-expression.
 */
public abstract class UnaryExpression implements Expression {

    private final Expression expression;

    public UnaryExpression(Expression expression) {
        this.expression = expression;
    }

    public <T> T acceptExpression(Visitor visitor) {
        return expression.accept(visitor);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("expression", expression)
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
        UnaryExpression that = (UnaryExpression) o;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression);
    }

}
