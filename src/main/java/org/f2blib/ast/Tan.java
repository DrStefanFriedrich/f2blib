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

import org.f2blib.visitor.DoubleVisitor;
import org.f2blib.visitor.Visitor;

/**
 * Tangens.
 */
public final class Tan extends UnaryExpression {

    public Tan(Expression expression) {
        super(expression);
    }

    public <T> T accept(Visitor visitor) {
        return visitor.visitTan(this);
    }

    @Override
    public double accept(DoubleVisitor visitor) {
        return visitor.visitTan(this);
    }

}
