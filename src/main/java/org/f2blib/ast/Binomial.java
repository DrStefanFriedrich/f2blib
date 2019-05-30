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
 * Binomial coefficient k over n.
 */
public class Binomial implements IntExpression {

    private static final int PRECEDENCE = 1;

    private final IntExpression k;

    private final IntExpression n;

    public Binomial(IntExpression k, IntExpression n) {
        this.k = k;
        this.n = n;
    }

    public IntExpression getK() {
        return k;
    }

    public IntExpression getN() {
        return n;
    }

    @Override
    public int precedence() {
        return PRECEDENCE;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("k", k)
                .add("n", n)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Binomial binomial = (Binomial) o;
        return Objects.equals(k, binomial.k) &&
                Objects.equals(n, binomial.n);
    }

    @Override
    public int hashCode() {

        return Objects.hash(k, n);
    }

    public <T> T accept(Visitor visitor) {
        return visitor.visitBinomial(this);
    }

}
