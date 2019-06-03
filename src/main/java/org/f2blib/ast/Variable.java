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
 * A variable is an expression of the form x_i, where i is a integer.
 */
public final class Variable extends IndexedExpression {

    public Variable(int index) {
        super(index);
    }

    public <T> T accept(Visitor visitor) {
        return visitor.visitVariable(this);
    }

}
