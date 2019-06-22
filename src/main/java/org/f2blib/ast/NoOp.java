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
 * An empty expression.
 */
public class NoOp implements Expression {

    private NoOp() {
    }

    private static final NoOp INSTANCE = new NoOp();

    public static NoOp get() {
        return INSTANCE;
    }

    @Override
    public <T> T accept(Visitor visitor) {
        return visitor.visitNoOp(this);
    }

    @Override
    public double accept(DoubleVisitor visitor) {
        return visitor.visitNoOp(this);
    }

}