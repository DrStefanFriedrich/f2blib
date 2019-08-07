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

/**
 * Models a variable name in a for loop.
 */
public final class ForVar implements IntExpression, Serializable {

    private final String variableName;

    public ForVar(String variableName) {
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("variableName", variableName)
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
        ForVar forVar = (ForVar) o;
        return Objects.equals(variableName, forVar.variableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variableName);
    }

    @Override
    public <T> T accept(Visitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean evaluatesToDouble() {
        return false;
    }

}
