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
 * Replace some variables by the outcome of the last computation. Consider a
 * function <code>f: IR^n -&gt; IR^m, n&gt;m</code> and an <code>offset&gt;=0
 * </code>. After <code>f(x)</code> has been calculated, the Markov Shift skips
 * the first <code>offset</code> variables in <code>x \in IR^n</code> and
 * shifts the remaining <code>n-offset</code> variables to the right by <code>
 * m</code> positions. Thus, <code>(x_(n-m+1), ..., x_n)</code> will be
 * discarded and <code>(x_(offset+1), ..., x_(offset+m))</code> will be filled
 * with the just calculated <code>f(x)</code>.
 */
public final class MarkovShift implements Serializable, ASTElement {

    private final IntExpression offset;

    public MarkovShift(IntExpression offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("offset", offset)
                .toString();
    }

    public IntExpression getOffset() {
        return offset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MarkovShift that = (MarkovShift) o;
        return Objects.equals(offset, that.offset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset);
    }

    @Override
    public <T> T accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
