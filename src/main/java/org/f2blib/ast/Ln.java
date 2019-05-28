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
 * Natural logarithm.
 */
public class Ln extends UnaryExpression {

    public Ln(Expression expression) {
        super(expression);
    }

    public void accept(Visitor visitor) {
        visitor.visitLn(this);
    }

}
