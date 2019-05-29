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

import java.util.Objects;

/**
 * Common abstract base class for Legendre and Laguerre polynomials.
 */
public abstract class SpecialPolynomialExpression implements Expression {

    private static final int PRECEDENCE = 0;

    private final IntExpression n;

    private final Expression expression;

    public SpecialPolynomialExpression(IntExpression n, Expression expression) {
        this.n = n;
        this.expression = expression;
    }

    public IntExpression getN() {
        return n;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public int precedence() {
        return PRECEDENCE;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("n", n)
                .add("expression", expression)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecialPolynomialExpression that = (SpecialPolynomialExpression) o;
        return Objects.equals(n, that.n) &&
                Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {

        return Objects.hash(n, expression);
    }
}
