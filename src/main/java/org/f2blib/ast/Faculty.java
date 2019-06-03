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
 * n!
 */
public final class Faculty implements IntExpression {

    private static final int PRECEDENCE = 1;

    private final IntExpression intExpression;

    public Faculty(IntExpression intExpression) {
        this.intExpression = intExpression;
    }

    public IntExpression getIntExpression() {
        return intExpression;
    }

    @Override
    public int precedence() {
        return PRECEDENCE;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("intExpression", intExpression)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return Objects.equals(intExpression, faculty.intExpression);
    }

    @Override
    public int hashCode() {

        return Objects.hash(intExpression);
    }

    public <T> T accept(Visitor visitor) {
        return visitor.visitFaculty(this);
    }

}
