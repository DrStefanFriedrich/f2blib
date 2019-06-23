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

import com.github.drstefanfriedrich.f2blib.visitor.DoubleVisitor;
import com.github.drstefanfriedrich.f2blib.visitor.Visitor;
import com.google.common.base.MoreObjects;
import com.github.drstefanfriedrich.f2blib.visitor.DoubleVisitor;
import com.github.drstefanfriedrich.f2blib.visitor.Visitor;

import java.util.Objects;

/**
 * n!
 */
public final class Faculty implements IntExpression {

    private final IntExpression intExpression;

    public Faculty(IntExpression intExpression) {
        this.intExpression = intExpression;
    }

    public <T> T acceptIntExpression(Visitor visitor) {
        return intExpression.accept(visitor);
    }

    public double acceptIntExpression(DoubleVisitor visitor) {
        return intExpression.accept(visitor);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("intExpression", intExpression)
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
        Faculty faculty = (Faculty) o;
        return Objects.equals(intExpression, faculty.intExpression);
    }

    @Override
    public int hashCode() {

        return Objects.hash(intExpression);
    }

    @Override
    public <T> T accept(Visitor visitor) {
        return visitor.visitFaculty(this);
    }

    @Override
    public double accept(DoubleVisitor visitor) {
        return visitor.visitFaculty(this);
    }

}
