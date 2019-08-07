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
 * Area cosine.
 */
public final class Arccos extends UnaryExpression {

    public Arccos(Expression expression) {
        super(expression);
    }

    @Override
    public <T> T accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
