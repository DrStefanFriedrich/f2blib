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
 * A {@link FunctionDefinition} is the top-level element in the abstract syntax
 * tree, and thus the main entry point for all function evaluation logic.
 */
public final class FunctionDefinition implements Serializable, ASTElement {

    private final String name;

    private final FunctionBody functionBody;

    public FunctionDefinition(String name, FunctionBody functionBody) {
        this.name = name;
        this.functionBody = functionBody;
    }

    public String getName() {
        return name;
    }

    /*
     * We violate the Law of Demeter in order to have a more fluent coding style
     * later in the visitors (i.e. Java Streams)
     */
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FunctionDefinition that = (FunctionDefinition) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(functionBody, that.functionBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, functionBody);
    }

    @Override
    public <T> T accept(Visitor visitor) {
        return visitor.visit(this);
    }

}
