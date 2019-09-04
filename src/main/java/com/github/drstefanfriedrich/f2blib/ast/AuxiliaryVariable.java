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

import java.io.Serializable;

public final class AuxiliaryVariable implements Serializable, ASTElement {

    private final AuxVar auxVar;

    private final Expression inner;

    public AuxiliaryVariable(AuxVar auxVar, Expression inner) {
        this.auxVar = auxVar;
        this.inner = inner;
    }

    @Override
    public <T> T accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public AuxVar getAuxVar() {
        return auxVar;
    }

    public <T> T acceptInner(Visitor visitor) {
        return inner.accept(visitor);
    }

    public <T> T acceptAuxVar(Visitor visitor) {
        return auxVar.accept(visitor);
    }

    public boolean evaluatesToDoublel() {
        return inner.evaluatesToDouble();
    }

}
