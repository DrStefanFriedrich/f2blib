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
 * A parameter is an expression of the form p_i, where i is a integer.
 * Semantically, variables are used for "variable" arguments, whereas parameters
 * are used more as additional information that will be supplied to the formula.
 * Technically, they are identical.
 */
public final class Parameter extends IndexedExpression {

    public Parameter(int index) {
        super(index);
    }

    @Override
    public <T> T accept(Visitor visitor) {
        return visitor.visitParameter(this);
    }

    @Override
    public double accept(DoubleVisitor visitor) {
        return visitor.visitParameter(this);
    }

}
