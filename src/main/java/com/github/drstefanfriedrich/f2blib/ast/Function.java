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
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Objects;

/**
 * A {@link Function} models a single-valued mathematical function. A function
 * consists of an index n and and expression: f_n := 'expression'.
 */
public final class Function implements Serializable, ASTElement {

    /*
     * The index is the subscript, e.g. f_1. Please not we start counting by 1
     * in function definitions, but the index starts at 0.
     */
    private final int index;

    private final Expression expression;

    public Function(int index, Expression expression) {
        this.index = index;
        this.expression = expression;
    }

    public boolean evaluatesToDouble() {
        return expression.evaluatesToDouble();
    }

    public int getIndex() {
        return index;
    }

    @VisibleForTesting
    public Expression getExpression() {
        return expression;
    }

    public <T> T acceptExpression(Visitor visitor) {
        return expression.accept(visitor);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("index", index)
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
        Function function = (Function) o;
        return index == function.index &&
                Objects.equals(expression, function.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, expression);
    }

    @Override
    public <T> T accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
