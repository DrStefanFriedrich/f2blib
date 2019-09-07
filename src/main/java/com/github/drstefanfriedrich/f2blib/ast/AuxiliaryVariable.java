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
import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Objects;

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

    public Expression getInner() {
        return inner;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("auxVar", auxVar)
                .add("inner", inner)
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
        AuxiliaryVariable that = (AuxiliaryVariable) o;
        return auxVar.equals(that.auxVar) &&
                inner.equals(that.inner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auxVar, inner);
    }

}
