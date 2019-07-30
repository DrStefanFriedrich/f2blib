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
 * An expression that evaluates two sub-expressions.
 */
public abstract class BinaryExpression implements Expression {

    private final Expression left;

    private final Expression right;

    public BinaryExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public <T> T acceptLeft(Visitor visitor) {
        return left.accept(visitor);
    }

    public <T> T acceptRight(Visitor visitor) {
        return right.accept(visitor);
    }

    public boolean leftEvaluatesToDouble() {
        return left.evaluatesToDouble();
    }

    public boolean rightEvaluatesToDouble() {
        return right.evaluatesToDouble();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("left", left)
                .add("right", right)
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
        BinaryExpression that = (BinaryExpression) o;
        return Objects.equals(left, that.left) &&
                Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public boolean evaluatesToDouble() {
        return leftEvaluatesToDouble() || rightEvaluatesToDouble();
    }

}
