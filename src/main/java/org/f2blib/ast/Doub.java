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
 * A {@link Doub} models a real-valued constant in the abstract syntax tree.
 */
public class Doub implements Expression {

    private final double value;

    public Doub(double value) {
        this.value = value;
    }

    public double getValue() {
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Doub doub = (Doub) o;
        return Double.compare(doub.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public <T> T accept(Visitor visitor) {
        return visitor.visitDoub(this);
    }

    @Override
    public double accept(DoubleVisitor visitor) {
        return visitor.visitDoub(this);
    }

}
