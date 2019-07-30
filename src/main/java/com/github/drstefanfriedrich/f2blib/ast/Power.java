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

/**
 * a^b.
 */
public final class Power extends BinaryExpression implements IntExpression {

    public Power(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public <T> T accept(Visitor visitor) {
        return visitor.visitPower(this);
    }

}
