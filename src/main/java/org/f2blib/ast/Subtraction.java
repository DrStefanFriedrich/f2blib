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

import org.f2blib.visitor.Visitor;

/**
 * Nomen est omen.
 */
public class Subtraction extends BinaryExpression {

    private static final int PRECEDENCE = 3;

    public Subtraction(Expression left, Expression right) {
        super(left, right);
    }

    public void accept(Visitor visitor) {
        visitor.visitSubtraction(this);
    }

    @Override
    public int precedence() {
        return PRECEDENCE;
    }

}
