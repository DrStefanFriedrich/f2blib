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

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Common base class for all &quot;variables&quot;.
 */
public abstract class AbstractVar {

    private final String variableName;

    public AbstractVar(String variableName) {
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
        AbstractVar abstractVar = (AbstractVar) o;
        return Objects.equals(variableName, abstractVar.variableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variableName);
    }

}
