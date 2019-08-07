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
 * Binomial coefficient n over k.
 */
public final class Binomial implements IntExpression {

    private final IntExpression n;

    private final IntExpression k;

    public Binomial(IntExpression n, IntExpression k) {
        this.k = k;
        this.n = n;
    }

    public <T> T acceptN(Visitor visitor) {
        return n.accept(visitor);
    }

    public <T> T acceptK(Visitor visitor) {
        return k.accept(visitor);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("n", n)
                .add("k", k)
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
        Binomial binomial = (Binomial) o;
        return Objects.equals(n, binomial.n) &&
                Objects.equals(k, binomial.k);
    }

    @Override
    public int hashCode() {

        return Objects.hash(n, k);
    }

    @Override
    public <T> T accept(Visitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean evaluatesToDouble() {
        return false;
    }

}
