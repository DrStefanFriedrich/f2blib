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

import com.google.common.base.MoreObjects;
import org.f2blib.visitor.Visitor;

import java.util.Objects;

/**
 * {@link FunctionDefinition} is the main entry point to the abstract syntax
 * tree (AST).
 */
public class FunctionDefinition {

    private final String name;

    private final FunctionBody functionBody;

    public FunctionDefinition(String name, FunctionBody functionBody) {
        this.name = name;
        this.functionBody = functionBody;
    }

    public String getName() {
        return name;
    }

    public FunctionBody getFunctionBody() {
        return functionBody;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("functionBody", functionBody)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionDefinition that = (FunctionDefinition) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(functionBody, that.functionBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, functionBody);
    }

    public void accept(Visitor visitor) {
        visitor.visitFunctionDefinition(this);
    }

}
