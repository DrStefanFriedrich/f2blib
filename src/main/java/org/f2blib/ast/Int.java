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
import org.f2blib.visitor.DoubleVisitor;
import org.f2blib.visitor.Visitor;

import java.util.Objects;

/**
 * The {@link Int} expression models an integer number.
 */
public final class Int implements IntExpression {

    private final int value;

    public Int(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("value", value)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Int anInt = (Int) o;
        return value == anInt.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public <T> T accept(Visitor visitor) {
        return visitor.visitInt(this);
    }

    @Override
    public double accept(DoubleVisitor visitor) {
        return visitor.visitInt(this);
    }

}
