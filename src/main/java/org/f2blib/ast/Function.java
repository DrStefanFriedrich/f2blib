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

package org.f2blib.ast;

import com.google.common.base.MoreObjects;
import org.f2blib.visitor.Visitor;

import java.util.Objects;

/**
 * {@link Function} models a single-valued mathematical function. A function has
 * an index n and and expression: f_n := 'expression'.
 */
public class Function implements ASTElement {

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

    public int getIndex() {
        return index;
    }

    public Expression getExpression() {
        return expression;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Function function = (Function) o;
        return index == function.index &&
                Objects.equals(expression, function.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, expression);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Void accept(Visitor visitor) {
        return visitor.visitFunction(this);
    }

}